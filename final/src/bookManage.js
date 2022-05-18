$(document).ready(function () {
    console.log('ready');

    get_book_list();
})

var book_list = [];

function get_book_list() {
    $.ajax({
        url: 'http://online_library.lllllan.cn/api/book/list.php',
        async: false, // 取消异步
        type: 'POST',
        dataType: 'json',
        data: {
            "phone": "12345678900",
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIkcGhvbmUiLCJpc3MiOiJhZG1pbiIsImlhdCI6MTY1MjcwNjgzMCwianRpIjoiZmI2NDIxMDQwNDJkNmQyMzkyOGVjODNhYWQ4OTNiMTMifQ.ULK9s4sbmwPwYlVh94uU66LkcQ5kPe0hnGqeIcZPG2Y",
        },
        success: function (data) {
            console.log(data);
            book_list = data.data;
        },
        error: function () {
            alert("sth wrong");
        }
    });

    show_book_list(book_list);
}

function show_book_list(list) {
    var data = "";

    for (var idx in list) {
        var item = list[idx];
        var row = "<tr>\
                    <td class='cctvmid' style='color: gray;'>"+ item._id + "</td>\
                    <td class='cctvmid' style='color: gray;'>"+ item.type + "</td>\
                    <td class='cctvmid'><img src='"+ item.img + "' class='img-circle' style='width:100%;'>\
                    </td>\
                    <td class='cctvmid' style='color: gray;'>"+ item.name + "</td>\
                    <td class='cctvmid' style='color: gray;'>"+ item.author + "</td>\
                    <td class='cctvmid' style='color: gray;'>"+ item.page + "</td>\
                    <td class='cctvmid' style='color: gray;'>"+ (item.cost == 1 ? "会员可读" : "免费") + "</td>\
                    <td class='cctvmid'>\
                        <button type='button' class='btn btn-success' style='margin-right: 10%;' data-toggle='modal'\
                            data-target='#"+ item._id + "'>编 辑</button>\
                        <div class='modal fade' id='"+ item._id + "' tabindex='-1' aria-labelledby='exampleModalLabel'\
                            aria-hidden='true'>\
                            <div class='modal-dialog'>\
                                <div class='modal-content'>\
                                    <div class='modal-header'>\
                                        <h5 class='modal-title' id='exampleModalLabel'>修改图书</h5>\
                                        <button type='button' class='close' data-dismiss='modal' aria-label='Close'>\
                                            <span aria-hidden='true'>&times;</span>\
                                        </button>\
                                    </div>\
                                    <div class='modal-body'>\
                                        <form>\
                                            <div class='mb-3'>\
                                                <label for='message-text' class='col-form-label'\
                                                    style='margin-right:5%;'>图书类型: </label>\
                                                <input type='text' class='form-control' id='newtype' value='"+ item.type + "'>\
                                            </div>\
                                            <div class='mb-3'>\
                                                <label for='message-text' class='col-form-label'\
                                                    style='margin-right:5%;'>图书作者: </label>\
                                                <input type='text' class='form-control' id='newauthor' value='"+ item.author + "'>\
                                            </div>\
                                            <div class='mb-3'>\
                                                <label for='message-text' class='col-form-label'\
                                                    style='margin-right:5%;'>收费情况: </label>\
                                                <input type='text' class='form-control' id='newper' value='"+ (item.cost == 1 ? "会员可读" : "免费") + "'>\
                                            </div>\
                                            <div class='modal-footer'>\
                                                <button type='button' class='btn btn-secondary' data-dismiss='modal'>关闭</button>\
                                                <button type='button' class='btn btn-primary'>确定</button>\
                                            </div>\
                                        </form>\
                                    </div>\
                                </div>\
                            </div>\
                        </div>\
                        <button type='button' class='btn btn-danger' onclick='book_delete("+ idx + ")'>删 除</button>\
                    </td>\
                </tr>"

        data += row;
    }

    $("tbody").html(data);
}

function book_add() {

}

function book_edit() {
}

function book_delete(idx) {

    var id = book_list[idx].id;
    book_list.splice(idx, 1);

    $.ajax({
        url: 'http://online_library.lllllan.cn/api/book/del.php',
        async: false, // 取消异步
        type: 'POST',
        dataType: 'json',
        data: {
            "phone": "12345678900",
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIkcGhvbmUiLCJpc3MiOiJhZG1pbiIsImlhdCI6MTY1MjcwNjgzMCwianRpIjoiZmI2NDIxMDQwNDJkNmQyMzkyOGVjODNhYWQ4OTNiMTMifQ.ULK9s4sbmwPwYlVh94uU66LkcQ5kPe0hnGqeIcZPG2Y",
            "id": id
        },
        success: function (data) {
            console.log(data);
        },
        error: function () {
            alert("sth wrong");
        }
    });

    show_book_list(book_list);
}

function search() {
    console.log("click search");
    console.log($("#name").val());

    var key = $("#name").val();
    var list = [];

    for(var idx in book_list) {
        var item = book_list[idx];
        if (item.type.includes(key) || item.name.includes(key) || item.author.includes(key)) {
            list.push(item);
        }
    }

    show_book_list(list);
}