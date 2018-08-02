/**
 * Created by Keno&Kemo on 10.12.2017..
 */
const getJsonUsers = "/adminPage/json-users";
const token = $('#_csrf').attr('content');
const header = $('#_csrf_header').attr('content');

let userIdToDelete;
let rowIndexToDelete;

$.ajaxSetup({
    headers: {
        'Content-Type':  'application/json',
        'Accept': 'application/json',
        'X-CSRF-TOKEN': token
    }
});

function setRowIndexAndUserId(row, id) {
    userIdToDelete = id;
    rowIndexToDelete = row.parentNode.parentNode.rowIndex;
}

function closeModal(nameOfTheModal) {
    $(nameOfTheModal).modal('toggle');
}

function deleteEntity() {
   
    let deleteUserUrl = '/adminPage/json-users/delete/' + userIdToDelete;

    $.ajax({
        url: deleteUserUrl,
        type: 'DELETE',
        success: function () {

            let table = $("#user-table");
            table[0].deleteRow(rowIndexToDelete);

            $('#alert-messages').append(
                "<div class='alert alert-success alert-dismissible fade show' role='alert'>"+
                "<button type='button' class='close' data-dismiss='alert' aria-label='Close'>"+
                "<span aria-hidden='true'>&times;</span> </button>"+
                "<strong>Well done!</strong> User has been deleted!!!"+
                "</div>"
            );
            closeModal('#deleteModal');
            userIdToDelete = "";
            rowIndexToDelete = "";
        }
    });
}

function searchUserByProperty() {
    let selectedProperty = $("#search-user-dropdown option:selected").text();
    let value = $("#searchUserBar").val();

    if (value != null && value !== "") {
        window.location.href = "/adminPage/users?usersProperty=" + selectedProperty + "&propertyValue=" + value;
    }

    else {
        window.location.href = "/adminPage/users";
    }

    //Old code:
    /*let getUsersByProperty = '/adminPage/json-users/search?usersProperty=' + selectedProperty + '&propertyValue=' + value;

    $.ajax({
        url: getUsersByProperty,
        type: 'GET',
        success: function (data, status, xhr) {

            let tableBody = $("#user-table-body");
            tableBody.empty();
            $.each(data, function (i, e) {
                let end = e.id + ");'";
                let del = "'setRowIndexAndUserId(this, " + end;
                let enabled;
                if (e.enabled === true) {
                    enabled = "<span style='color: green'>Enabled</span>"
                }
                else enabled = "<span style='color: red'>Disabled</span>";

                let row = $('<tr>').append(
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
                $('#user-table-body').append(row);
            });
        },
        error: function (jqXhr, textStatus, errorMessage) {
            let httpStatusCode = jqXhr.status;

            if(httpStatusCode === 404){
                $('#alert-messages').append(
                    "<div class='alert alert-info alert-dismissible fade show' role='alert'>"+
                    "<button type='button' class='close' data-dismiss='alert' aria-label='Close'>"+
                    "<span aria-hidden='true'>&times;</span> </button>"+
                    "Sorry, no matches found for "+ selectedProperty + " = " + value +
                    "</div>"
                );
            }
        }
    });*/
}



