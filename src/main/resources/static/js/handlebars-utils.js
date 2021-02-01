

//  math
//  i+1 = {{math @index "+" 1}} 
//  i-0.5 = {{math @index "+" "-0.5"}} 
//  i/2 = {{math @index "*" 2}} 
//  i%2 = {{math @index "%" 2}} 
//  i*i = {{math @index "*" @index}}

Handlebars.registerHelper("math", function(lvalue, operator, rvalue, options) {
    lvalue = parseFloat(lvalue);
    rvalue = parseFloat(rvalue);
        
    return {
        "+": lvalue + rvalue,
        "-": lvalue - rvalue,
        "*": lvalue * rvalue,
        "/": parseInt(lvalue / rvalue),
        "%": lvalue % rvalue
    }[operator];
});



Handlebars.registerHelper("pagerowindex", function(pageIndex, pageSize, index) {
	        
    return (pageIndex-1) * pageSize + index +1;
});



Handlebars.registerHelper('pagination', function(currentPage, totalPage, size, options) {
	  var startPage, endPage, context;

	  if (arguments.length === 3) {
	    options = size;
	    size = 5;
	  }

	  startPage = Math.floor( (currentPage - 1) / size) * size + 1 ;
	  endPage = (Math.floor( (currentPage - 1) / size) + 1) * size;
	  	
	  if (startPage <= 0) {
	    endPage -= (startPage - 1);
	    startPage = 1;
	  }

	  if (endPage > totalPage) {
	    endPage = totalPage;
	  }
	  
	  prevPage = startPage - size;
	  nextPage = startPage + size;


	  context = {
	    startFromFirstPage: false,
		firstPage : 1,
	    
		prevShow: true,
	    prevPage: prevPage,
	    
	    pages: [],
	    
	    nextShow: true,
	    nextPage: nextPage,
	    
	    endAtLastPage: false,
		lastPage : totalPage,
	  };
	  
	  if (prevPage <= 0) {
		  context.prevPage = 1;
		  //context.prevShow = false;
	  }
	  
	  if (nextPage > totalPage) {
		  context.nextPage = totalPage;
		  //context.nextShow  = false;
	  }

	  
//	  if (startPage === 1) {
//	    context.startFromFirstPage = true;
//	  }
	  for (var i = startPage; i <= endPage; i++) {
	    context.pages.push({
	      page: i,
	      isCurrent: i === currentPage,
	    });
	  }
	  /*if (endPage === totalPage) {
	    context.endAtLastPage = true;
	  }*/

	  return options.fn(context);
});

Handlebars.registerHelper('breaklines', function(descriptionFunction) {
    text = descriptionFunction;
    text = Handlebars.Utils.escapeExpression(text);
    text = text.toString();
    text = text.replace(/(\r\n|\n|\r|￦n)/gm, '<br>');
    return new Handlebars.SafeString(text);
});

Handlebars.registerHelper('var', function(name, value, context){
	  this[name] = value;
	});

Handlebars.registerHelper('rootVar', function(name, value, context){
	  context.data.root[ name ] = value;
	});


Handlebars.registerHelper('ifCond', function (v1, operator, v2, options) {

    switch (operator) {
        case '==':
            return (v1 == v2) ? options.fn(this) : options.inverse(this);
        case '===':
            return (v1 === v2) ? options.fn(this) : options.inverse(this);
        case '!=':
            return (v1 != v2) ? options.fn(this) : options.inverse(this);
        case '!==':
            return (v1 !== v2) ? options.fn(this) : options.inverse(this);
        case '<':
            return (v1 < v2) ? options.fn(this) : options.inverse(this);
        case '<=':
            return (v1 <= v2) ? options.fn(this) : options.inverse(this);
        case '>':
            return (v1 > v2) ? options.fn(this) : options.inverse(this);
        case '>=':
            return (v1 >= v2) ? options.fn(this) : options.inverse(this);
        case '&&':
            return (v1 && v2) ? options.fn(this) : options.inverse(this);
        case '||':
            return (v1 || v2) ? options.fn(this) : options.inverse(this);
        default:
            return options.inverse(this);
    }
});

Handlebars.registerHelper('format', function(value) {
    return value.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
});


Handlebars.registerHelper('formatTime', function (date, format) {
	if(date ){
		var mmnt = moment(date);
		return mmnt.format(format);
	}
	return "";
});

var rno = 0;
Handlebars.registerHelper('nodeRightNo', function(index, options){
	if(index > 0){
		if(rno < this.LEFT_NO){
			rno = this.RIGHT_NO;
			return options.fn(this);
		}else{
			return options.inverse(this);
		}
	}else{
		rno = this.RIGHT_NO;
		return options.fn(this);
	}
});

Handlebars.registerHelper('isNodeNotes', function(options){
	if(this.NOTES == ""){
		return options.fn(this);
	}else{
		return options.inverse(this);
	}
});

Handlebars.__switch_stack__ = [];
Handlebars.registerHelper( "switch", function( value, options ) {
	Handlebars.__switch_stack__.push({
		switch_match : false,
		switch_value : value
	});
	var html = options.fn( this );
	Handlebars.__switch_stack__.pop();
	return html;
} );
Handlebars.registerHelper( "case", function( value, options ) {
	var args = Array.prototype.slice.call(arguments);
	var options = args.pop();
	var caseValues = args;
	var stack = Handlebars.__switch_stack__[Handlebars.__switch_stack__.length - 1];
	
	if ( stack.switch_match || caseValues.indexOf( stack.switch_value ) === -1 ) {
		return '';
	} else {
		stack.switch_match = true;
		return options.fn( this );
	}
} );
Handlebars.registerHelper( "default", function( options ) {
	var stack = Handlebars.__switch_stack__[Handlebars.__switch_stack__.length - 1];
	if ( !stack.switch_match ) {
	return options.fn( this );
	}
} );

Handlebars.registerHelper('checkPage', function(lastPage, options) {
    if(lastPage > 10) {
      return options.fn(this);
    }
    return options.inverse(this);
 });
