/**
 * Created on 08.04.16.
 */
define(["jquery"], function ($) {

	const PROPERTIES = {
		DISABLED: 'disabled'
	};

	function disable(jQuerySelector) {
		$.each(arguments, function (index, value) {
			$(value).prop(PROPERTIES.DISABLED, true);
		});
	}

	function enable(jQuerySelector) {
		$.each(arguments, function (index, value) {
			$(value).prop(PROPERTIES.DISABLED, false);
		});
	}

	function show(jQuerySelector) {
		$.each(arguments, function (index, value) {
			$(value).show();
		});
	}

	function hide(jQuerySelector) {
		$.each(arguments, function (index, value) {
			$(value).hide();
		});
	}

	function emptyValue(jQuerySelector) {
		$.each(arguments, function (index, value) {
			$(value).val('');
		});
	}

	function addClass(className, jQuerySelector) {
		for (var i = 1; i < arguments.length; i++) {
			$(arguments[i]).addClass(arguments[0]);
		}
	}

	function removeClass(className, jQuerySelector) {
		for (var i = 1; i < arguments.length; i++) {
			$(arguments[i]).removeClass(arguments[0]);
		}
	}

	function addId(id, jQuerySelector) {
		$(jQuerySelector).attr('id', id);
	}

	return {
		disable: disable,
		enable: enable,
		show: show,
		hide: hide,
		emptyValue: emptyValue,
		addClass: addClass,
		removeClass: removeClass,
		addId: addId
	}
});
