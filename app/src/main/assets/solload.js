function loadBootstrap(){
    $("body").css("margin", '0');
    $("body").css("margin-top", "60px");
    $("body").css("word-break", "break-word");

    var background = $("body").css("background-color");

    var head = $('head');
    head.append('<meta name="viewport" content="width=device-width, initial-scale=1">');
    var body = $('body');
    var originalBody = body.children();
    body.append("<div class='container' id='container'></div>");
    $("#container").append(originalBody);

    body.prepend( '<nav class="navbar navbar-inverse navbar-fixed-top">' + 
    '  <div class="container-fluid">' + 
    '    <div class="navbar-header">' + 
    '    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">' + 
    '            <span class="sr-only">Toggle navigation</span>' + 
    '            <span class="icon-bar"></span>' + 
    '            <span class="icon-bar"></span>' + 
    '            <span class="icon-bar"></span>' + 
    '          </button>' + 
    '      <a class="navbar-brand" href="http://www.samuraioflegend.com">Samurai Of Legend</a>' + 
    '    </div>' + 
    '    <div id="navbar" class="navbar-collapse collapse">' + 
    '      <ul class="nav navbar-nav" id="menu-options">' + 
    '        <li class="dropdown">' + 
    '                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Other Links<span class="caret"></span></a>' + 
    '                <ul class="dropdown-menu" id="otherLinks">' + 
    '                  ' + 
    '                </ul>' + 
    '              </li>' + 
    '      </ul>' + 
    '      <ul class="nav navbar-nav navbar-right">' + 
    '        <li><a href="#contactPopup" data-toggle="modal" data-target="#contactPopup"><span class="glyphicon glyphicon-ok-circle"> </span> Status</a></li>' + 
    '        <li class="dropdown">' + 
    '                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-cog"> Player/Account<span class="caret"></span></a>' + 
    '                <ul class="dropdown-menu" id="account-options">' + 
    '                  </ul>' + 
    '              </li>' + 
    '      </ul>' + 
    '    </div>' + 
    '  </div>' + 
    '</nav>'  + 
    '<div id="contactPopup" class="modal fade" role="dialog">' + 
     '  <div class="modal-dialog" >' + 
     '' + 
     '    <!-- Modal content-->' + 
     '    <div class="modal-content" background="#003366">' + 
     '      <div class="modal-header">' + 
     '        <button type="button" class="close" data-dismiss="modal">&times;</button>' + 
     '        <h4 class="modal-title">Player Status</h4>' + 
     '      </div>' + 
     '      <div class="modal-body"  id="statusBody" style="background-color:  #003366">' + 
     '       ' + 
     '      </div>' + 
     '      <div class="modal-footer">' + 
     '        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>' + 
     '      </div>' + 
     '    </div>' + 
     '' + 
     '  </div>' + 
     '</div>');
    var links = jQuery('td[width="260"]');
    if(links.length === 0){
        links = jQuery('td[width="180"]');
    }
    if(links.length === 0){
        links = jQuery('td[background="bg.png"]');
    }
    if(links.length === 0){
        links = jQuery('.sidebar');
    }
    if(links.length === 0){
        links = jQuery('.col_1');
    }
    if(links.length === 0){
        links = jQuery('.tiny4');
    }
    console.log(links);
    links.attr('hidden', 'true');
    links.attr('width', '0');
    var linkArray = links.find("a");
    for(index = 0; index < linkArray.length; index++){
        var linkText = linkArray.get(index).innerHTML;
        if(linkText.indexOf('Mailbox') !== -1 || linkText.indexOf('You Have Mail') !== -1 || linkText.indexOf('Events') !== -1 || linkText.indexOf('Home') !== -1 || linkText.indexOf('Inventory') !== -1){
             $('#menu-options').prepend('<li>' + linkArray.get(index).outerHTML + '</li>');
        }
        else if(linkArray.length - index < 7){
            $('#account-options').append('<li>' + linkArray.get(index).outerHTML + '</li>');
        }
        else {
            $('#otherLinks').append('<li>' + linkArray.get(index).outerHTML + '</li>');
        }
            
    }
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

loadBootstrap();