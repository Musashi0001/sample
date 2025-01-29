document.addEventListener("DOMContentLoaded", function() {
	const tabs = document.querySelectorAll(".tab-link");
	const contents = document.querySelectorAll(".tab-content");

	tabs.forEach(tab => {
		tab.addEventListener("click", function() {
			// タブのアクティブクラスを更新
			tabs.forEach(t => t.classList.remove("active"));
			this.classList.add("active");

			// コンテンツの表示を更新
			contents.forEach(content => content.classList.remove("active"));
			document.getElementById(this.dataset.tab).classList.add("active");
		});
	});
});
