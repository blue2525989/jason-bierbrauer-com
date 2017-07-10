/* open modal */
function openModal() {
	document.getElementById('myModal').style.display="block";
}

/* close modal */
function closeModal() {
	document.getElementById('myModal').style.display="none";
}

/* intialize and set slideIndex to 1 */
var slideIndex = 1;
/* call showSlides */
showSlides(slideIndex);

/* increase slideIndex by 1 */
function plusSlides(n) {
	showSlides(slideIndex += n);
}

/* show current slide */
function currentSlide(n) {
	showSlides(slideIndex = n);
}

/* show slides */
function showSlides(n) {
	var i;
	var slides = document.getElementsByClassName("mySlides");
	var dots = document.getElementsByClassName("card-dots");
	var captionText = document.getElementById("caption");
	if (n > slides.length) {slideindex = 1}
	if (n < 1) {slideIndex = slides.length}
	for (i = 0; i < slides.length; i++) {
		slides[i].style.display = "none";
	}
	for (i = 0; i < dots.length; i++) {
		dots[i].className= dots[i].className.replace("active", "");
	}
	slides[slideIndex-1].style.display = "block";
	captionText.innerHTML = dots[slideIndex-1].alt;
}