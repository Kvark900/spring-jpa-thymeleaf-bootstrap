/**
 * Created by Keno&Kemo on 10.12.2017..
 */

var url = "/adminPage/";
var tableTarget = url + "users";

function showDeleteModal(index) {
    $('#delete-id').val(index);
}


function closeModal(name) {
    $(name).modal('toggle');
}


function deleteEntity(entity) {
    var input = $('#delete-id');
    var url = '/' + entity + '/delete/' + input.val();
    $.post(url, function (data) {
        updateTable(data);
    });
    closeModal('#deleteModal');
    input.val('');
}

function updateTable(data) {
    $.ajax({
        dataType: "json",
        url: tableTarget,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'GET',
        success: function (response) {
            $('#table-body').empty();
            $.each(response, function (i, e) {
                var end = e.id + ");'";
                var del = "showDeleteModal(" + end;
                var row = $('<tr>').append(
                    $('<td>').text(e.id),
                    $('<td>').text(e.name),
                    $('<td>').text(e.surname),
                    $('<td>').text(e.username),
                    $('<td>').text(e.email),
                    $('<td>').append(
                        "<button type='button' class='btn btn-outline-danger' data-toggle='modal' " +
                "data-target='#deleteModal' onclick = del > Remove </button>"
                        )
                );
                $('#user-table').append(row);
            });
        }
    });
}
