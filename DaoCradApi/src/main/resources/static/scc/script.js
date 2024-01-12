// Открытие модального окна с изображением
function openModal(imgSrc) {
  var modal = document.getElementById('myModal');
  var modalImg = document.getElementById('modalImg');

  modal.style.display = 'block';
  modalImg.src = imgSrc;
}

// Закрытие модального окна
function closeModal() {
  var modal = document.getElementById('myModal');
  modal.style.display = 'none';
}