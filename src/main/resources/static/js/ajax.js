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

            $('#delete-alert').append(
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


