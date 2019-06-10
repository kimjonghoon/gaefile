<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    //Suppose the article number is 1099
    pageContext.setAttribute("group", "1099");

    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    String uploadUrl = blobstoreService.createUploadUrl("/files/1099");
    pageContext.setAttribute("uploadUrl", uploadUrl);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8"/>
<title>Ajax File Upload</title>
<link rel="stylesheet" href="/resources/stylesheets/main.css" type="text/css" />
<script src="/resources/js/jquery-3.2.1.min.js"></script>
<script>
function displayComments() {
    var url = '/comments/';
    $.getJSON(url, function (data) {
        $('#all-comments').empty();
        $.each(data, function (i, item) {
            var creation = new Date(item.creation);
            var comments = '<div class="comments">'
                    + '<span class="writer">' + item.username + '</span>'
                    + '<span class="date">' + creation.toLocaleString() + '</span>';
            if (item.editable === true) {
                comments = comments
                        + '<span class="modify-del">'
                        + '<a href="#" class="comment-modify-link">Modify</a> |'
                        + '<a href="#" class="comment-delete-link" title="' + item.commentNo + '">Del</a>'
                        + '</span>';
            }
            comments = comments
                    + '<p class="comment-p">' + item.content + '</p>'
                    + '<form class="comment-form" action="/comments/' + item.commentNo + '" method="put" style="display: none;">'
                    + '<div style="text-align: right;">'
                    + '<a href="#" class="comment-modify-submit-link">Submit</a> | <a href="#" class="comment-modify-cancel-link">Cancel</a>'
                    + '</div>'
                    + '<div>'
                    + '<textarea class="comment-textarea" name="content" rows="7" cols="50">' + item.content + '</textarea>'
                    + '</div>'
                    + '</form>'
                    + '</div>';
            $('#all-comments').append(comments);
            console.log(item);
        });
    });
}

function displayFiles() {
    var url = '/files/${group}';
    $.getJSON(url, function (data) {
        $('#all-files').empty();
        $.each(data, function (i, item) {
            var file = '<div class="file">';
            file += '<a href="#" title="' + item.id + '" class="download">' + item.filename + '</a>';
            if (item.deletable === true) {
                file += '&nbsp;&nbsp;<a href="#" title="' + item.id + '">X</a>';
            }
            file += '</div>';
            $('#all-files').append(file);
            console.log(item);
        });
    });
}

$(document).ready(function () {
    displayComments();
    displayFiles();
    //new comment
    $("#addCommentForm").submit(function (event) {
        event.preventDefault();
        var $form = $(this);
        var content = $('#addComment-ta').val();
        content = $.trim(content);
        if (content.length === 0) {
            $('#addComment-ta').val('');
            return false;
        }
        var dataToBeSent = $form.serialize();
        var url = $form.attr("action");
        var posting = $.post(url, dataToBeSent);
        posting.done(function () {
            displayComments();
            $('#addComment-ta').val('');
        });
    });

    $('article > iframe').attr('allowFullScreen', '');

});

$(document).on('click', '#all-comments', function (e) {
    if ($(e.target).is('.comment-modify-link')) {
        e.preventDefault();
        var $form = $(e.target).parent().parent().find('.comment-form');
        var $p = $(e.target).parent().parent().find('.comment-p');

        if ($form.is(':hidden') === true) {
            $form.show();
            $p.hide();
        } else {
            $form.hide();
            $p.show();
        }
    } else if ($(e.target).is('.comment-modify-cancel-link')) {
        e.preventDefault();
        var $form = $(e.target).parent().parent().parent().find('.comment-form');
        var $p = $(e.target).parent().parent().parent().find('.comment-p');

        if ($form.is(':hidden') === true) {
            $form.show();
            $p.hide();
        } else {
            $form.hide();
            $p.show();
        }
    } else if ($(e.target).is('.comment-modify-submit-link')) {
        e.preventDefault();
        var $form = $(e.target).parent().parent().parent().find('.comment-form');
        var $textarea = $(e.target).parent().parent().find('.comment-textarea');
        var content = $textarea.val();
        $('#modifyCommentForm input[name*=content]').val(content);
        var dataToBeSent = $('#modifyCommentForm').serialize();
        var url = $form.attr("action");
        $.ajax({
            url: url,
            type: 'POST',
            data: dataToBeSent,
            success: function () {
                displayComments();
            },
            error: function () {
                alert('error!');
            }
        });
    } else if ($(e.target).is('.comment-delete-link')) {
        e.preventDefault();
        var msg = 'Are you sure you want to delete this item?';
        var chk = confirm(msg);
        if (chk === false) {
            return;
        }
        var $commentNo = $(e.target).attr('title');
        var url = $('#deleteCommentForm').attr('action');
        url += $commentNo;
        var dataToBeSent = $('#deleteCommentForm').serialize();
        $.ajax({
            url: url,
            type: 'POST',
            data: dataToBeSent,
            success: function () {
                displayComments();
            },
            error: function (error) {
                alert('Error');
                console.log(error);
                console.log(error.status);
            }
        });
    }
});

$(document).on('click', '#all-files', function (e) {
    if ($(e.target).is('a.download')) {
        e.preventDefault();
        var id = $(e.target).attr('title');
        var action = '/files/${group}/' + id; 
        $('#downloadForm').attr('action', action);
        $('#downloadForm').submit();
    } else if ($(e.target).is('a:not(.download)')) {
        e.preventDefault();
        var msg = 'Are you sure you want to delete this item?';
        var chk = confirm(msg);
        if (chk === false) {
            return;
        }
        var id = $(e.target).attr('title');
        var url = '/files/${group}/' + id;
        var dataToBeSent = $('#deleteFileForm').serialize();
        $.ajax({
            url: url,
            type: 'POST',
            data: dataToBeSent,
            success: function () {
                displayFiles();
            },
            error: function (error) {
                alert('Error');
                console.log(error);
                console.log(error.status);
            }
        });
    }
});
</script>
</head>

<body>

<div id="wrap">

    <article>
        <iframe width="854" height="480" src="https://www.youtube.com/embed/Ph5NOf-di18"></iframe>
    </article>
    <form:form id="addCommentForm" action="/comments" method="post">
        <div id="addComment">
            <textarea id="addComment-ta" name="content" rows="2" cols="50"></textarea>
        </div>
        <div style="text-align: right;">
            <input type="submit" value="Submit" />
        </div>
    </form:form>

    <div id="all-comments"></div>

    <div id="all-files"></div>

    <form:form id="fileForm" action="${uploadUrl}" method="post" enctype="multipart/form-data">
        <div><input type="file" name="attachFile" /><input type="submit" value="Submit" /></div>
    </form:form>

</div>

<div id="form-group" style="display: none">
    <form:form id="deleteCommentForm" action="/comments/" method="delete">
        <input type="hidden" name="_method" value="DELETE" />
    </form:form>
    <form:form id="modifyCommentForm" method="put">
        <input type="hidden" name="_method" value="PUT" />
        <input type="hidden" name="content" />
    </form:form>
    <form:form id="deleteFileForm" method="delete">
        <input type="hidden" name="_method" value="DELETE" />
    </form:form> 
    <form:form id="downloadForm" method="get">
    </form:form>
</div>

</body>
</html>