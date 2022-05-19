$(document).ready(function () {
    console.log('ready');

    get_book_list();
})

var book_list = [];
var list = [];
var cover;

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

    for (var idx in book_list) {
        list.push(book_list[idx]);
    }

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
                            data-target='#modal"+ item.id + "'>编 辑</button>\
                        <div class='modal fade' id='modal"+ item.id + "' tabindex='-1' aria-labelledby='exampleModalLabel'\
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
                                                    style='margin-right:5%;white-space: nowrap; '>图书类型: </label>\
                                                <input type='text' class='form-control' id='type"+ item.id + "' value='" + item.type + "'>\
                                            </div>\
                                            <div class='mb-3'>\
                                                <label for='message-text' class='col-form-label'\
                                                    style='margin-right:5%;white-space: nowrap; '>图书名称: </label>\
                                                <input type='text' class='form-control' id='name"+ item.id + "' value='" + item.type + "'>\
                                            </div>\
                                            <div class='mb-3'>\
                                                <label for='message-text' class='col-form-label'\
                                                    style='margin-right:5%;white-space: nowrap; '>图书作者: </label>\
                                                <input type='text' class='form-control' id='author"+ item.id + "' value='" + item.author + "'>\
                                            </div>\
                                            <div class='mb-3'>\
                                                <label for='message-text' class='col-form-label'\
                                                    style='margin-right:5%;white-space: nowrap; '>收费情况: </label>\
                                                <input type='text' class='form-control' id='per"+ item.id + "r' value='" + (item.cost == 1 ? "会员可读" : "免费") + "'>\
                                            </div>\
                                            <div class='mb-3'>\
                                                <label for='message-text' class='col-form-label'\
                                                    style='margin-right:5%;white-space: nowrap; '>图书简介: </label>\
                                                <input type='text' class='form-control' id='introduction"+ item.id + "' value='" + item.introduction + "'>\
                                            </div >\
                                            <div class='mb-3'>\
                                                <label for='message-text' class='col-form-label'\
                                                style='margin-right:5%;white-space: nowrap; '>图书内容: </label>\
                                            <input type='text' class='form-control' id='content"+ item.id + "' value='" + item.content + "'>\
                                            </div>\
                                            <div class='control-group'>\
                                                <label class='control-label' for='FileUpload1'>上传图书封面</label>\
                                                <div class='controls'>\
                                                    <input class='input-file uniform_on' id='cover"+ item.id + "' name='FileUpload1' type='file' onchange='change_cover(this.files, " + idx + ")'>\
                                                </div>\
                                            </div>\
                                            <div class='modal-footer'>\
                                                <button type='button' class='btn btn-secondary' data-dismiss='modal'>关闭</button>\
                                                <button type='button' class='btn btn-primary' onclick='book_edit("+ idx + ")'>确定</button>\
                                            </div>\
                                        </form >\
                                    </div >\
                                </div >\
                            </div >\
                        </div >\
                        <button type='button' class='btn btn-danger' onclick='book_delete("+ idx + ")'>删 除</button>\
                    </td >\
                </tr > "

        data += row;
    }

    $("tbody").html(data);
}


function upload(file) {
    var img = "";

    $.ajax({
        url: 'http://online_library.lllllan.cn/api/upload.php',
        async: false, // 取消异步
        type: 'POST',
        dataType: 'json',
        data: file,
        cache: false,
        processData: false,
        contentType: false,
        success: function (data) {
            console.log(data);
            img = data.data;
        },
        error: function () {
            alert("sth wrong");
        }
    });

    return img;
}

// 添加图书的图片上传
function upload_cover(files) {
    let cover_file = new FormData();
    cover_file.append('file', files[0]);
    cover = upload(cover_file);
}

function change_cover(files, idx) {
    console.log("click change cover");
    console.log(idx);

    let cover_file = new FormData();
    cover_file.append('file', files[0]);

    var img = upload(cover_file);

    for (var i in book_list) {
        if (book_list[i] == list[idx]) {
            book_list[i].img = img;
            break;
        }
    }
    list[idx].img = img;
}

function book_add() {
    console.log("click book add");
    console.log($('#add_cover').val());
    console.log(cover == null ? "cover is null" : cover);

    var type = $("#newtype").val();
    var name = $("#newname").val();
    var author = $("#newauthor").val();
    var cost = ($("#newper").val() == '免费') ? 0 : 1;
    var img = (cover == null) ? "" : cover;
    var introduction = $("#newintro").val();
    var content = $("#newcontent").val();

    console.log(type, name, author, cost, img, introduction, content);

    $.ajax({
        url: 'http://online_library.lllllan.cn/api/book/add.php',
        async: false, // 取消异步
        type: 'POST',
        dataType: 'json',
        data: {
            "phone": "12345678900",
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIkcGhvbmUiLCJpc3MiOiJhZG1pbiIsImlhdCI6MTY1MjcwNjgzMCwianRpIjoiZmI2NDIxMDQwNDJkNmQyMzkyOGVjODNhYWQ4OTNiMTMifQ.ULK9s4sbmwPwYlVh94uU66LkcQ5kPe0hnGqeIcZPG2Y",
            "type": type,
            "author": author,
            "content": content,
            "cost": cost,
            "img": img,
            "name": name,
            "introduction": introduction
        },
        success: function (data) {
            console.log(data);
        },
        error: function () {
            alert("sth wrong");
        }
    });

    location.reload();
}

function book_edit(idx) {
    console.log("click book edit");

    var id = list[idx].id;
    var type = $("#type" + list[idx].id).val();
    var name = $("#name" + list[idx].id).val();
    var author = $("#author" + list[idx].id).val();
    var cost = ($("#per" + list[idx].id).val() == '免费') ? 0 : 1;
    var img = list[idx].img;
    var introduction = $("#introduction" + list[idx].id).val();
    var content = $("#content" + list[idx].id).val();

    console.log(id, type, name, author, cost, img, introduction, content);

    $.ajax({
        url: 'http://online_library.lllllan.cn/api/book/mod.php',
        async: false, // 取消异步
        type: 'POST',
        dataType: 'json',
        data: {
            "phone": "12345678900",
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIkcGhvbmUiLCJpc3MiOiJhZG1pbiIsImlhdCI6MTY1MjcwNjgzMCwianRpIjoiZmI2NDIxMDQwNDJkNmQyMzkyOGVjODNhYWQ4OTNiMTMifQ.ULK9s4sbmwPwYlVh94uU66LkcQ5kPe0hnGqeIcZPG2Y",
            "id": id,
            "type": type,
            "author": author,
            "content": content,
            "cost": cost,
            "img": img,
            "name": name,
            "introduction": introduction
        },
        success: function (data) {
            console.log(data);
        },
        error: function () {
            alert("sth wrong");
        }
    });
    
    location.reload();
}

function book_delete(idx) {

    var id = list[idx].id;
    for (var i in book_list) {
        var item = book_list[i];
        if (item == list[idx]) {
            console.log(i, "->", idx);
            book_list.splice(i, 1);
            break;
        }
    }
    list.splice(idx, 1);

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

    show_book_list(list);
}

function search() {
    console.log("click search");
    console.log($("#name").val());

    var key = $("#name").val();
    list = [];

    for (var idx in book_list) {
        var item = book_list[idx];
        if (item.type.includes(key) || item.name.includes(key) || item.author.includes(key) || (item.cost == 1 ? "会员可读" : "免费").includes(key)) {
            list.push(item);
        }
    }

    show_book_list(list);
}