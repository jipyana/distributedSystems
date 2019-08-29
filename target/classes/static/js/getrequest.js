
$(document).ready(function () {
  var card = {};
  var dynamicURL = "";
  var methodName = "";
  getAllCards();
  $('#btnAddCard').click(function () {
      card["nameOfCard"] = $('#nameOfCard').val();
      card["description"] = $('#description').val();
      var cardId =  card["id"] = $('#id').val();
    //   if(cardId){
    //       //update it
    //       dynamicURL = "http://localhost:8080/cards/save";
    //       methodName = "PUT";
    //     }else{
    //      //save it
    //       dynamicURL = "http://localhost:8080/cards/"+cardId;
    //       methodName = "POST";
    //   }
      var cardObj = JSON.stringify(card);
      $.ajax({
          url: 'http://localhost:8080/cards/save',
          method: 'POST',
          data: cardObj,
          contentType: 'application/json; charset=utf-8',
          success: function () {
              alert('Saved successfully');
              getAllCards();
              reset();
          },
          error: function (error) {
              alert(error);
          }
      })
  })
})

function getAllCards() {
  $.ajax({
      url: "http://localhost:8080/cards/all",
      method: "GET",
      dataType: "json",
      success: function (data) {
          var tableBody = $('#tblCards tbody');
          tableBody.empty();
          $(data).each(function (index, card) {
              tableBody.append('<tr><td>'+card["nameOfCard"]+'</td><td>'+card["description"]+'</td><td><button class="btn btn-primary" onclick = "update('+card["id"]+')">Update</button> | <button class="btn btn-primary" onclick = "deleteCard('+card["id"]+')">Delete</button></td></tr>');
          })
      },
      error: function (error) {
          alert(error);
      }
  })
}

function deleteCard(id){
  $.ajax({
      url: 'http://localhost:8080/cards/'+id,
      method: 'DELETE',
      success: function () {
          alert('record has been deleted');
          getAllCards();
      },
      error: function (error) {
          alert(error);
      }
  })
}

function update(id){
  $.ajax({
      url: "http://localhost:8080/cards/"+id,
      method: 'GET',
      dataType: 'json',
      success: function (data) {
          $('#nameOfCard').val(data.nameOfCard);
          $('#description').val(data.description);
          $('#id').val(data.id);
          getAllCards();
      },
      error: function (error) {
          alert(error);
      }
  })
}

function reset(){
  $('#nameOfCard').val('');
  $('#description').val('');
  $('#id').val('');
}