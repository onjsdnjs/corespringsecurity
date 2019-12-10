<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title>Member Register</title>
    <link rel="stylesheet" href="/resources/css/bootstrap.min.css">
	<link rel="stylesheet" href="/resources/css/bootstrap-table.css">
	<script src="/resources/js/jquery-2.1.3.min.js"></script>
	<script src="/resources/js/bootstrap.min.js"></script>
	<script src="/resources/js/bootstrap-table.js"></script>
</head>

<body>
	<script type="text/javascript">

	$(document).ready(function(){
	
		var $table = $('#table'),
		$button = $('#button');
		var ids;
		
		  	$('#myModal').on('show', function() {
			    $('#myModal .modal-body p').html("Member UID : [" + ids + "] <br><br> Do you want to delete really?");
			    var id = $(this).data('id'),
			    removeBtn = $(this).find('.danger');
			})

			/* $('.btn-default').on('click', function(e) {
			    e.preventDefault();

			    var id = $(this).data('memberUid');
			    $('#myModal').data('memberUid', id).modal('show');
			}); */

			$('#btnYes').click(function() {
			    // handle deletion here
			    
			    $table.bootstrapTable('remove', {
	                field: 'memberUid',
	                values: ids
	            });
			    $('#myModal').modal('hide');
			    
			});
		
			 $button.click(function (e) {
			 	e.preventDefault();
			 	
	            ids = $.map($table.bootstrapTable('getSelections'), function (row) {
	                return row.memberUid;
	            });
	            var id = $(this).data('memberUid');
	            $('#myModal').data('memberUid', id).modal('show');
	            
	        });
		
	});
	
	</script>
	
		<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
           <div class="modal-header">
               <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
               <h3 id="myModalLabel">Delete</h3>
           </div>
           <div class="modal-body">
               <p></p>
           </div>
           <div class="modal-footer">
               <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
               <button data-dismiss="modal" class="btn red" id="btnYes">Confirm</button>
           </div>
   		</div>
	
		<div class="hero-unit">
			<h1>Member List</h1>
		</div>
		<div id="toolbar">
            <button id="button" class="btn btn-default">Delete</button>
        </div>
		<div class="row-fluid" style="padding-left:100px;">
			<div class="span8">
				<table id="table" data-toggle="table" data-toolbar="#toolbar" data-url="/rest/member/list" data-height="300" data-show-refresh="true" 
				data-search="true"data-pagination="true" data-page-list="[5, 10, 20, 50, 100, 200]">
				    <thead>
				    <tr>
				        <th data-field="state" data-checkbox="true"></th>
				        <th data-field="memberUid" data-align="center" data-sortable="true">Member UID</th>
				        <th data-field="nickName" data-align="center" data-sortable="true">NickName</th>
				        <th data-field="passwd" data-align="center" data-sortable="true">Password</th>
				        <th data-field="email" data-align="center" data-sortable="true">Email</th>
				    </tr>
				    </thead>
				</table>
				<div style="text-align:right;">
				<p><a class="btn btn-primary btn-large" style="margin:10;" href="/member/register">Member Register </a></p>
				</div>
 			</div>
		</div>
		
</body>
</html>