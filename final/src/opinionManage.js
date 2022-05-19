$(document).ready(function () {
    console.log('ready');

    get_opinion_list();
})

// 从数据库获取的所有意见列表
var opinion_list = [];
// 当前页面显示的意见列表
var list = [];

// 获取所有的意见信息
function get_opinion_list() {
    $.ajax({
        url: 'http://online_library.lllllan.cn/api/opinion/list.php',
        async: false, // 取消异步
        type: 'POST',
        dataType: 'json',
        data: {
            "phone": "12345678900",
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIkcGhvbmUiLCJpc3MiOiJhZG1pbiIsImlhdCI6MTY1MjcwNjgzMCwianRpIjoiZmI2NDIxMDQwNDJkNmQyMzkyOGVjODNhYWQ4OTNiMTMifQ.ULK9s4sbmwPwYlVh94uU66LkcQ5kPe0hnGqeIcZPG2Y",
            "type": "1"
        },
        success: function (data) {
            console.log(data);
            opinion_list = data.data;
        },
        error: function () {
            alert("sth wrong");
        }
    });

    for (var idx in opinion_list) {
        list.push(opinion_list[idx]);
    }

    show_opinion_list(opinion_list);
}

// 显示(筛选的)意见信息
function show_opinion_list(list) {
    var data = "";

    for (var idx in list) {
        var item = list[idx];

        var row = "<div class='opinion'>\
                        <div class='photo'>\
                            <img src='"+ item.user_img + "' width='30' height='30' alt='photo'>\
                        </div>\
                        <div class='contentpiece'>\
                            <div class='contenthead'>\
                                <div class='headbasic'>\
                                    <div class='nameid'>\
                                        <h6 id='name' style='margin-right: 10px;color: #333;'>"+ item.user_name + "</h6>\
                                        <h6 id='userid' style='color: #333;'>"+ item.user_id + "</h6>\
                                    </div>\
                                    <div class='time'>\
                                        <p style='font-size: small;color:#979FAC;'>"+ item.op_submit + "</p>\
                                    </div>\
                                </div>\
                                <div class='headmodify'>\
                                    <img src='../static/img/pencil.svg' width='20' height='20' alt='pencil'\
                                        type='button' style='margin-right: 20px;' data-toggle='modal'\
                                        data-target='#modal"+ item.op_id + "'>\
                                    <img src='../static/img/bin.svg' width='20' height='20' alt='bin' type='button' onclick='op_delete("+ idx + ")'>\
                                    <!--增加模态框 -->\
                                    <div class='modal fade' id='modal"+ item.op_id + "' tabindex='-1'\
                                        aria-labelledby='exampleModalLabel' aria-hidden='true'>\
                                        <div class='modal-dialog'>\
                                            <div class='modal-content'>\
                                                <div class='modal-header'>\
                                                    <h5 class='modal-title' id='exampleModalLabel'>回复意见</h5>\
                                                    <button type='button' class='close' data-dismiss='modal'\
                                                        aria-label='Close'>\
                                                        <span aria-hidden='true'>&times;</span>\
                                                    </button>\
                                                </div>\
                                                <div class='modal-body'>\
                                                    <form>\
                                                        <div class='mb-3'>\
                                                            <label for='recipient-name' class='col-form-label'\
                                                                style='margin-right:5%;'>回复内容: </label>\
                                                            <input type='text' class='form-control' id='deal"+ item.op_id + "' value='" + (item.op_deal == null ? "" : item.op_deal) + "'>\
                                                        </div>\
                                                        <div class='modal-footer'>\
                                                            <button type='button' class='btn btn-secondary'\
                                                                data-dismiss='modal'>关闭</button>\
                                                            <button type='button' class='btn btn-primary' onclick='op_deal("+ idx + ")'>确定</button>\
                                                        </div>\
                                                    </form>\
                                                </div>\
                                            </div>\
                                        </div>\
                                    </div>\
                                </div>\
                            </div>\
                            <div class='contentbody'>\
                                <p class='card-text'>"+ item.op_content + "</p>\
                            </div>\
                        </div>\
                    </div>\
                    <div class='opinion' style='background-color: rgb(249, 249, 249) ;'>\
                        <div class='contentpiece'>\
                                <div class='contentbody'>\
                                    <p class='card-text'>"+ (item.op_deal == null ? "" : item.op_deal) + "</p>\
                                </div>\
                            </div>\
                        </div>\
                    </div>";

        data += row;
    }

    $("#opinion_content").html(data);
}

function op_deal(idx) {
    console.log("click op deal");

    var id = list[idx].op_id;
    var deal = $("#deal" + id).val();

    console.log(id, deal);

    $.ajax({
        url: 'http://online_library.lllllan.cn/api/opinion/deal.php',
        async: false, // 取消异步
        type: 'POST',
        dataType: 'json',
        data: {
            "phone": "12345678900",
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIkcGhvbmUiLCJpc3MiOiJhZG1pbiIsImlhdCI6MTY1MjcwNjgzMCwianRpIjoiZmI2NDIxMDQwNDJkNmQyMzkyOGVjODNhYWQ4OTNiMTMifQ.ULK9s4sbmwPwYlVh94uU66LkcQ5kPe0hnGqeIcZPG2Y",
            "id": id,
            "deal": deal
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

function op_delete(idx) {
    console.log("click op delete");

    var id = list[idx].op_id;

    for (var i in opinion_list) {
        var item = opinion_list[i];
        if (item == list[idx]) {
            console.log(i, " -> ", idx);
            opinion_list.splice(i, 1);
            break;
        }
    }

    list.splice(idx, 1);
    show_opinion_list(list);

    $.ajax({
        url: 'http://online_library.lllllan.cn/api/opinion/delete.php',
        async: false, // 取消异步
        type: 'POST',
        dataType: 'json',
        data: {
            "phone": "12345678900",
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIkcGhvbmUiLCJpc3MiOiJhZG1pbiIsImlhdCI6MTY1MjcwNjgzMCwianRpIjoiZmI2NDIxMDQwNDJkNmQyMzkyOGVjODNhYWQ4OTNiMTMifQ.ULK9s4sbmwPwYlVh94uU66LkcQ5kPe0hnGqeIcZPG2Y",
            "id": id,
        },
        success: function (data) {
            console.log(data);
        },
        error: function () {
            alert("sth wrong");
        }
    });
}

function search() {
    console.log("click search");
    console.log($("#name").val());

    var key = $("#name").val();
    list = [];

    for (var idx in opinion_list) {
        var item = opinion_list[idx];
        if (item.user_name.includes(key) || item.op_content.includes(key)) {
            list.push(item);
        }
    }

    show_opinion_list(list);
}