/* 复制代码到剪贴板 */
function copyToClipboard(btnId) {
	var btn = document.getElementById(btnId);
	var clipboard = new Clipboard(btn);//实例化

	//复制成功执行的回调，可选
	clipboard.on('success', function(e) {
		console.info('Text:', e.text);
	});

	//复制失败执行的回调，可选
	clipboard.on('error', function(e) {
		console.log(e);
	});
}