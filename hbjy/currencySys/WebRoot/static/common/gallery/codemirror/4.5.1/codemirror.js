define(function(require){

	/**
     * @name Codemirror
     * @class 一款“Online Source Editor”，基于Javascript，短小精悍，实时在线代码高亮显示，他不是某个富文本编辑器的附属产品，他是许多大名鼎鼎的在线代码编辑器的基础库。<a href="http://codemirror.net//" target="_blank" title="homepage">主页</a>
     * @version v4.5.1
     */
	
	/**
     * @name Codemirror#fromTextArea
     * @function   
     * @desc 代码渲染。
     * @param {Object} options
     * @example
     * define(function(require) {
     *     $(function() {
     *         require('codemirror');
     *         var myEditor = CodeMirror.fromTextArea($uiCode[0],{
     *     	       lineNumbers: true,
     *     	       styleActiveLine: true,
     *     	       matchBrackets: true,
     *     	       autoCloseTags: true,
     *     	       mode: "htmlmixed"
     *         });
     *         myEditor('change', function(){
     *     	       //TODO
     *         });				
     * });
     */
	
	
	require('./codemirror.css');
	require('./show-hint.css');
	require('./codemirror-master');
	require('./show-hint');
	require('./sql-hint');
	require('./sql');
});