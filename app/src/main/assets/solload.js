function loadBootstrap(){
    $("body").css("margin", '0');
    $("body").css("margin-top", "60px");
    $("body").css("word-break", "break-word");

    var background = $("body").css("background-color");

    var head = $('head');
    var body = $('body');

    $(".linegrad").attr('hidden', 'true').attr('width', '0');
    $(".lgrad").attr('hidden', 'true').attr('width', '0');
    $("table").attr('width', '100%').attr('max-width', '1000px');
    $("td").attr('width', null).attr('min-width', '100px');
    $("th").attr('width', null).attr('min-width', '100px').attr('background-repeat', 'repeat');
    body.attr('background-color', $(".left").attr('bgcolor'));
    $( "img" ).addClass( "img-responsive" );
    $( ".click" ).removeClass( "img-responsive" );


    var upperTable = $(".left").find("table").first();
    if(upperTable.length === 0){
        upperTable = $(".maintable").find("tbody").find("tr").first();
        console.log(upperTable);
        $(".maintable").css('width', '100%');
    }
    if(upperTable.length === 0){
        upperTable = $(".header");
        console.log(upperTable);
        $(".wrapper").css('width', '100%');
        $(".wrapper").css('margin-left', '0');
        $(".gamecontent").css('width', '100%');
        $(".gamecontent").css('margin-left', '0');
        $("body").css("background-image", 'none');
        $("body").css("color", '#ffffff');
    }
    if(upperTable.length === 0){
        upperTable = $(".stats_box");
        console.log(upperTable);
        $(".stats_box").css('width', '100%');
        $(".footer").css('width', '100%');
        $(".adspace_728x90").css('width', '100%');
        $(".wrapper_ingame").css('width', '100%');
    }
    if(upperTable.length === 0){
        upperTable = $(".table7");
        console.log(upperTable);
        $("body").css('background-color', '#000000');
        $("#container").css('background-color', '#000000');
    }
    var tableCells = upperTable.find('td');
    $('#statusBody').append('<table>');
    for(tdIndex = 0; tdIndex < tableCells.length; tdIndex++){
        $('#statusBody').append('<tr>' + tableCells[tdIndex].outerHTML + '</tr>');
    }
    $('#statusBody').append('</table>');
    upperTable.attr('hidden', 'true');
    $('#statusBody').find('img').removeClass('img-responsive');
    $('#statusBody').find('img').first().addClass('img-responsive');

    $("body").css("background-color", background);
    $("textarea").attr("cols", '30').attr('width', '100%');
    $(":submit").addClass('btn btn-default btn-block');
    $(".submit").attr('padding-right', '0').attr('padding-left', '0');
    $("#container").attr('margin', '0');
    $("#container").attr('padding-left', '0px');
    $("#container").attr('padding-right', '0px');
}

$(loadBootstrap());