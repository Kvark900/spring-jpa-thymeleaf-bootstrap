/**
 * Created by Keno&Kemo on 10.12.2017..
 */

var getJsonUsers = "/adminPage/json-users";
var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');


$.ajaxSetup({
    headers: {
        'Content-Type':  'application/json',
        'Accept': 'application/json',
        'X-CSRF-TOKEN': token
    }
});

function getUserId(index) {
    $('#delete-id').val(index);
}

function closeModal(nameOfTheModal) {
    $(nameOfTheModal).modal('toggle');
}

function deleteEntity() {
    var input = $('#delete-id');
    var deleteUserUrl = '/adminPage/json-users/delete/' + input.val();

    $.ajax({
        url: deleteUserUrl,
        type: 'DELETE',
        success: function () {
            updateTable();
            $('#delete-alert').append(
                "<div class='alert alert-success alert-dismissible fade show' role='alert'>"+
                "<button type='button' class='close' data-dismiss='alert' aria-label='Close'>"+
                "<span aria-hidden='true'>&times;</span> </button>"+
                "<strong>Well done!</strong> User has been deleted!!!"+
                "</div>"
            );
            closeModal('#deleteModal');
            input.val('');
        }
    });
}

function updateTable() {
    $.ajax({
        dataType: "json",
        url: getJsonUsers,
        type: 'GET',
        cache: false,

        success: function (data, status, xhr) {
            $('#table-body').empty();
            $.each(data, function (i, e) {
                var end = e.id + ");'";
                var del = "'getUserId(" + end;
                var enabled;
                if (e.enabled === true) {
                    enabled = "<span style='color: green'>Enabled</span>"
                }
                else enabled = "<span style='color: red'>Disabled</span>";

                var row = $('<tr>').append(
                    $('<td>').text(e.id),
                    $('<td>').text(e.name),
                    $('<td>').text(e.surname),
                    $('<td>').text(e.username),
                    $('<td>').text(e.email),
                    $('<td>').append(enabled),
                    $('<td>').append(
                        "<a style='text-decoration: none; color:blue' href='/adminPage/users/" + e.id + "'" +
                        "class='editBtn' data-toggle='tooltip' data-placement='right' title='Edit user'>" +
                        "<i class='fa fa-edit'></i></a>"
                    ),
                    $('<td>').append(
                        "<a id='remove-link' style='text-decoration: none; color:red'" +
                        "data-toggle='modal' data-placement='right' title='Remove user' " +
                        "data-target='#deleteModal' onclick=" +
                        del + "><i class='fa fa-times' aria-hidden='true'></i></a>"
                    )
                );
                $('#table-body').append(row);
            });
        },
        error: function (jqXhr, textStatus, errorMessage) {
            console.log("AJAX ERROR: " + errorMessage +"\n"+
                        "TEXT STATUS: " + textStatus + "\n"+
                        "JQXHR: " + jqXhr);
        }
    });
}


