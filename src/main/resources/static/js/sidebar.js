function confirmDelete() {
	if (confirm("本当にこのアカウントを削除しますか？")) {
		document.getElementById('delete-form').submit();
	}
}

document.addEventListener("DOMContentLoaded", function() {
	const sidebar = document.getElementById("sideMenu");

	document.addEventListener("mousemove", function(event) {
		if (event.clientX < 50) { // 左端 50px 以内
			sidebar.style.left = "0px";
		} else if (event.clientX > 200) { // メニューを離れたら
			sidebar.style.left = "-200px";
		}
	});
});