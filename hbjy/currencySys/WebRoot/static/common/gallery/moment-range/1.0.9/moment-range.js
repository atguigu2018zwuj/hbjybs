define("gallery/moment-range/1.0.9/moment-range",["moment"],function(a){a("moment");var b,c;return c={year:!0,month:!0,week:!0,day:!0,hour:!0,minute:!0,second:!0},b=function(){function a(a,b){this.start=moment(a),this.end=moment(b)}return a.prototype.clone=function(){return moment().range(this.start,this.end)},a.prototype.contains=function(b,c){return b instanceof a?this.start<=b.start&&(this.end>b.end||this.end.isSame(b.end)&&!c):this.start<=b&&(this.end>b||this.end.isSame(b)&&!c)},a.prototype._by_string=function(a,b,c){var d,e;for(d=moment(this.start),e=[];this.contains(d,c);)b.call(this,d.clone()),e.push(d.add(1,a));return e},a.prototype._by_range=function(a,b,c){var d,e,f,g,h;if(d=this/a,f=Math.floor(d),1/0===f)return this;for(f===d&&c&&(f-=1),h=[],e=g=0;f>=0?f>=g:g>=f;e=f>=0?++g:--g)h.push(b.call(this,moment(this.start.valueOf()+a.valueOf()*e)));return h},a.prototype.overlaps=function(a){return null!==this.intersect(a)},a.prototype.intersect=function(b){var c,d,e,f,g,h,i,j;return this.start<=(d=b.start)&&d<(c=this.end)&&c<b.end?new a(b.start,this.end):b.start<(f=this.start)&&f<(e=b.end)&&e<=this.end?new a(this.start,b.end):b.start<(h=this.start)&&h<=(g=this.end)&&g<b.end?this:this.start<=(j=b.start)&&j<=(i=b.end)&&i<=this.end?b:null},a.prototype.add=function(b){return this.overlaps(b)?new a(moment.min(this.start,b.start),moment.max(this.end,b.end)):null},a.prototype.subtract=function(b){var c,d,e,f,g,h,i,j;return null===this.intersect(b)?[this]:b.start<=(d=this.start)&&d<(c=this.end)&&c<=b.end?[]:b.start<=(f=this.start)&&f<(e=b.end)&&e<this.end?[new a(b.end,this.end)]:this.start<(h=b.start)&&h<(g=this.end)&&g<=b.end?[new a(this.start,b.start)]:this.start<(j=b.start)&&j<(i=b.end)&&i<this.end?[new a(this.start,b.start),new a(b.end,this.end)]:void 0},a.prototype.by=function(a,b,c){return"string"==typeof a?this._by_string(a,b,c):this._by_range(a,b,c),this},a.prototype.valueOf=function(){return this.end-this.start},a.prototype.center=function(){var a;return a=this.start+this.diff()/2,moment(a)},a.prototype.toDate=function(){return[this.start.toDate(),this.end.toDate()]},a.prototype.isSame=function(a){return this.start.isSame(a.start)&&this.end.isSame(a.end)},a.prototype.diff=function(a){return null==a&&(a=void 0),this.end.diff(this.start,a)},a}(),moment.range=function(a,d){return a in c?new b(moment(this).startOf(a),moment(this).endOf(a)):new b(a,d)},moment.range.constructor=b,moment.fn.range=moment.range,moment.fn.within=function(a){return a.contains(this._d)},moment});
