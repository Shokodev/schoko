(function(t){function e(e){for(var s,o,a=e[0],c=e[1],l=e[2],p=0,d=[];p<a.length;p++)o=a[p],Object.prototype.hasOwnProperty.call(i,o)&&i[o]&&d.push(i[o][0]),i[o]=0;for(s in c)Object.prototype.hasOwnProperty.call(c,s)&&(t[s]=c[s]);u&&u(e);while(d.length)d.shift()();return r.push.apply(r,l||[]),n()}function n(){for(var t,e=0;e<r.length;e++){for(var n=r[e],s=!0,a=1;a<n.length;a++){var c=n[a];0!==i[c]&&(s=!1)}s&&(r.splice(e--,1),t=o(o.s=n[0]))}return t}var s={},i={app:0},r=[];function o(e){if(s[e])return s[e].exports;var n=s[e]={i:e,l:!1,exports:{}};return t[e].call(n.exports,n,n.exports,o),n.l=!0,n.exports}o.m=t,o.c=s,o.d=function(t,e,n){o.o(t,e)||Object.defineProperty(t,e,{enumerable:!0,get:n})},o.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},o.t=function(t,e){if(1&e&&(t=o(t)),8&e)return t;if(4&e&&"object"===typeof t&&t&&t.__esModule)return t;var n=Object.create(null);if(o.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:t}),2&e&&"string"!=typeof t)for(var s in t)o.d(n,s,function(e){return t[e]}.bind(null,s));return n},o.n=function(t){var e=t&&t.__esModule?function(){return t["default"]}:function(){return t};return o.d(e,"a",e),e},o.o=function(t,e){return Object.prototype.hasOwnProperty.call(t,e)},o.p="/";var a=window["webpackJsonp"]=window["webpackJsonp"]||[],c=a.push.bind(a);a.push=e,a=a.slice();for(var l=0;l<a.length;l++)e(a[l]);var u=c;r.push([0,"chunk-vendors"]),n()})({0:function(t,e,n){t.exports=n("56d7")},"034f":function(t,e,n){"use strict";var s=n("85ec"),i=n.n(s);i.a},"56d7":function(t,e,n){"use strict";n.r(e);n("e260"),n("e6cf"),n("cca6"),n("a79d");var s=n("2b0e"),i=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{attrs:{id:"app"}},[n("section",{staticClass:"columns is-fullheight"},[n("aside",{staticClass:"column is-narrow is-fullheight",attrs:{id:"menu"}},[n("p",{staticClass:"menu-label"},[t._v("Menu")]),n("Menu")],1),n("router-view",{staticClass:"column",attrs:{id:"content"}})],1)])},r=[],o=(n("a4d3"),n("4de4"),n("4160"),n("e439"),n("dbb4"),n("b64b"),n("159b"),n("2fa7")),a=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("ul",{staticClass:"menu-list"},[n("li",[n("router-link",{attrs:{to:"/home"},on:{click:t.getToHome}},[t._v("Home")])],1),n("li",[n("router-link",{attrs:{to:"/alarmList"},on:{click:t.AlarmList}},[t._v("Alarmliste")])],1),n("li",[n("router-link",{attrs:{to:"/structure"},on:{click:t.getHierarchy}},[t._v("Struktur")])],1),n("li",[n("router-link",{attrs:{to:"/settings"},on:{click:t.settings}},[t._v("Einstellungen")])],1)])},c=[],l={name:"Menu",methods:{getToHome:function(){console.log("getToHome")},AlarmList:function(){console.log("AlarmList")},getHierarchy:function(){console.log("getHierarchy")},device:function(){console.log("device")},settings:function(){console.log("settings")}}},u=l,p=n("2877"),d=Object(p["a"])(u,a,c,!1,null,null,null),f=d.exports,m=n("2f62");function v(t,e){var n=Object.keys(t);if(Object.getOwnPropertySymbols){var s=Object.getOwnPropertySymbols(t);e&&(s=s.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),n.push.apply(n,s)}return n}function h(t){for(var e=1;e<arguments.length;e++){var n=null!=arguments[e]?arguments[e]:{};e%2?v(n,!0).forEach((function(e){Object(o["a"])(t,e,n[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(n)):v(n).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(n,e))}))}return t}var g={name:"app",components:{Menu:f},methods:h({},Object(m["b"])(["readSettings"])),created:function(){this.readSettings()}},b=g,_=(n("034f"),Object(p["a"])(b,i,r,!1,null,null,null)),C=_.exports,y=n("8c4f"),O=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",[n("div",[n("h1",{staticClass:"subtitle is 3"},[t._v(" Home ")]),n("span",[n("span",{staticClass:"button is-fullwidth level-left ",attrs:{id:"postsButton"},on:{click:t.goBack}},[t._v(t._s(t.posts.elementName)+" ("+t._s(t.posts.elementDescription)+")")])])]),n("div",{staticClass:"container is-fluid"},[n("div",[0!==t.posts.length?n("span",t._l(t.posts.children,(function(e){return n("span",{key:e.elementName,staticClass:"button is-fullwidth level-left",attrs:{id:"childButton"},on:{click:function(n){return t.goIn(e)}}},[t._v(" "+t._s(e.elementName)),""!==e.elementDescription?n("span",{attrs:{id:"description"}},[t._v("("+t._s(e.elementDescription)+")")]):t._e()])})),0):t._e()]),t.isModalVisible?n("modal",{attrs:{node:t.root},on:{close:function(e){t.isModalVisible=!1}}}):t._e()],1)])},S=[],w=(n("99af"),n("ac1f"),n("5319"),n("bc3a")),j=n.n(w),x=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"modal-mask"},[n("div",{staticClass:"modal-wrapper"},[n("div",{staticClass:"modal-container"},[n("article",{staticClass:"media"},[n("div",{staticClass:"media-content"},[n("div",{staticClass:"content"},[n("strong",[t._v("Datapoint")]),n("ul",t._l(t.bacnetObject,(function(e){return n("li",{key:e.elementName},[t._v(" "+t._s(e.propertyIdentifier)+" "+t._s(e.value)+" ")])})),0)]),t._m(0)])]),n("button",{staticClass:"button",on:{click:function(e){return t.$emit("close")}}},[t._v(" OK ")]),n("button",{staticClass:"button",on:{click:t.connect}},[t._v(" Connect ")])])])])},k=[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"field has-addons has-addons-centered box"},[n("div",{staticClass:"level-left"},[n("strong",[t._v("Aktueller Wert")])]),n("p",{staticClass:"control level-right"},[n("span",{staticClass:"select"},[n("select",[n("option",[t._v("Ein")]),n("option",[t._v("Aus")])])])]),n("p",{staticClass:"control"},[n("a",{staticClass:"button is-primary"},[t._v(" Senden ")])])])}],A=n("74d1"),B=n.n(A),D=n("cc7d"),N=n.n(D),P=null,E={name:"modal",props:["node"],data:function(){return{bacnetObject:"",status:"disconnected"}},mounted:function(){this.bacnetObject=this.node},methods:{connect:function(){cd;var t=new N.a("/ws/Object");P=B.a.over(t),P.connect({},(function(t){console.log("Connected: "+t)}))}}},H=E,$=(n("f01e"),Object(p["a"])(H,x,k,!1,null,null,null)),M=$.exports,T={name:"structure",components:{modal:M},data:function(){return{posts:{},element:"Anlage",parent:"",firstElement:!0,properties:[],isModalVisible:!1,root:[],child:{elementDescription:""}}},mounted:function(){this.loadJSON()},methods:{loadJSON:function(){var t=this;j.a.get("http://localhost:8098/structure/"+this.element).then((function(e){t.posts=e.data,t.parent=t.posts["elementName"],t.firstElement=!1}))},goBack:function(){this.element=this.element.replace("'"+this.parent,""),this.loadJSON()},goIn:function(t){console.log(t),!1===this.isBACnetObject(t)&&(console.log(t),!1===this.firstElement&&(this.element=this.element.concat("'"+t["elementName"]),console.log(this.element),this.loadJSON()))},isBACnetObject:function(t){return console.log(t["elementType"]),"Structure Element"!==t["elementType"]&&(console.log("reading BACnet Object"),this.isModalVisible=!0,!0)}}},J=T,R=(n("be3c"),Object(p["a"])(J,O,S,!1,null,"03a87b92",null)),L=R.exports,V=function(){var t=this,e=t.$createElement,n=t._self._c||e;return t.getHierarchy?n("div",[n("h1",{staticClass:"subtitle is 3"},[t._v(" BACnet Struktur ")]),n("TreeBrowser",{attrs:{node:t.getHierarchy}})],1):t._e()},I=[],F=function(){var t=this,e=t.$createElement,n=t._self._c||e;return t.node.children?n("div",[n("div",{staticClass:"node",style:{"margin-left":20*t.depth+"px"},on:{click:t.nodeClicked}},[t.hasChildren?n("span",{staticClass:"type"},[t._v(" "+t._s(t.expanded?"▼":"►")+" ")]):n("span",{staticClass:"type"},[t._v(" "+t._s(t.expanded="◇")+" ")]),n("span",{style:t.getStyle(t.node)},[t._v(t._s(t.node.elementName)+" "),t.hasDescription?n("span",[t._v(" ("+t._s(t.node.elementDescription)+")")]):t._e()])]),t.expanded?n("div",t._l(t.node.children,(function(e){return n("TreeBrowser",{key:e.elementName,attrs:{node:e,depth:t.depth+1},on:{onClick:function(e){return t.$emit("onClick",e)}}})})),1):t._e()]):t._e()},K=[],W=(n("a9e3"),{name:"TreeBrowser",props:{node:Object,depth:{type:Number,default:0}},data:function(){return{expanded:!1}},methods:{nodeClicked:function(){this.expanded=!this.expanded,this.hasChildren||this.$emit("onClick",this.node)},getStyle:function(t){if("undefined"!=typeof this.node.children){var e="black";return t.children||(e="red"),{color:e}}}},computed:{hasChildren:function(){return this.node.children.length>0},hasDescription:function(){return""!==this.node["elementDescription"]}}}),q=W,z=(n("ac03"),Object(p["a"])(q,F,K,!1,null,"04601375",null)),G=z.exports,Q={name:"structure",methods:Object(m["b"])(["completeHierarchy"]),computed:Object(m["c"])(["getHierarchy"]),created:function(){this.completeHierarchy()},components:{TreeBrowser:G}},U=Q,X=Object(p["a"])(U,V,I,!1,null,"3c972360",null),Y=X.exports,Z=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",[n("p",{staticClass:"subtitle is 3"},[t._v("Einstellungen")]),n("div",{staticClass:"control"},[n("div",{staticClass:"field"},[n("label",{staticClass:"label"},[t._v("Site Name")]),n("div",{staticClass:"control"},[n("input",{directives:[{name:"model",rawName:"v-model",value:t.settings.siteName,expression:"settings.siteName"}],staticClass:"input",attrs:{type:"text"},domProps:{value:t.settings.siteName},on:{input:function(e){e.target.composing||t.$set(t.settings,"siteName",e.target.value)}}})]),n("p",{staticClass:"help"},[t._v("Site Namen eingeben")])]),n("div",{staticClass:"field"},[n("label",{staticClass:"label"},[t._v("Site Beschreibung")]),n("div",{staticClass:"control"},[n("input",{directives:[{name:"model",rawName:"v-model",value:t.settings.siteDescription,expression:"settings.siteDescription"}],staticClass:"input",attrs:{type:"text"},domProps:{value:t.settings.siteDescription},on:{input:function(e){e.target.composing||t.$set(t.settings,"siteDescription",e.target.value)}}})]),n("p",{staticClass:"help"},[t._v("Site Description eingeben")])]),n("div",{staticClass:"field"},[n("label",{staticClass:"label"},[t._v("BACnet Seperator")]),n("div",{staticClass:"control"},[n("input",{directives:[{name:"model",rawName:"v-model",value:t.settings.bacnetSeperator,expression:"settings.bacnetSeperator"}],staticClass:"input",attrs:{type:"text",placeholder:"'"},domProps:{value:t.settings.bacnetSeperator},on:{input:function(e){e.target.composing||t.$set(t.settings,"bacnetSeperator",e.target.value)}}})]),n("p",{staticClass:"help"},[t._v("BACnet Seperator eingeben")])]),n("div",{staticClass:"control"},[n("label",{staticClass:"label"},[t._v("BACnet Port")]),n("div",{staticClass:"select"},[n("select",{directives:[{name:"model",rawName:"v-model",value:t.settings.port,expression:"settings.port"}],on:{change:function(e){var n=Array.prototype.filter.call(e.target.options,(function(t){return t.selected})).map((function(t){var e="_value"in t?t._value:t.value;return e}));t.$set(t.settings,"port",e.target.multiple?n:n[0])}}},[n("option",[t._v("0xBAC0")]),n("option",[t._v("0xBAC1")]),n("option",[t._v("0xBAC2")]),n("option",[t._v("0xBAC3")]),n("option",[t._v("0xBAC4")]),n("option",[t._v("0xBAC5")]),n("option",[t._v("0xBAC6")]),n("option",[t._v("0xBAC7")]),n("option",[t._v("0xBAC8")]),n("option",[t._v("0xBAC9")]),n("option",[t._v("0xBACA")]),n("option",[t._v("0xBACB")]),n("option",[t._v("0xBACC")]),n("option",[t._v("0xBACD")]),n("option",[t._v("0xBACE")]),n("option",[t._v("0xBACF")])])]),n("p",{staticClass:"help"},[t._v("BACnet port auswählen")])]),n("div",{staticClass:"field is-grouped"},[n("div",{staticClass:"control"},[n("button",{staticClass:"button is-link",on:{click:function(e){return t.SetSettings()}}},[t._v("Speichern")])]),t._m(0)])])])},tt=[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"control"},[n("button",{staticClass:"button is-text"},[t._v("Abbrechen")])])}];function et(t,e){var n=Object.keys(t);if(Object.getOwnPropertySymbols){var s=Object.getOwnPropertySymbols(t);e&&(s=s.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),n.push.apply(n,s)}return n}function nt(t){for(var e=1;e<arguments.length;e++){var n=null!=arguments[e]?arguments[e]:{};e%2?et(n,!0).forEach((function(e){Object(o["a"])(t,e,n[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(n)):et(n).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(n,e))}))}return t}var st={name:"Settings",data:function(){return{settings:{siteName:"DefaultSite",port:Number,siteDescription:"Desciption",bacnetSeperator:"'"}}},computed:nt({},Object(m["c"])(["getSettings"])),methods:nt({},Object(m["b"])(["newSettings"]),{SetSettings:function(){this.newSettings(this.settings),console.log("send")}}),mounted:function(){this.settings.siteDescription=this.getSettings.siteDescription,this.settings.siteName=this.getSettings.siteName,this.settings.port=this.getSettings.port,this.settings.bacnetSeperator=this.getSettings.bacnetSeperator}},it=st,rt=Object(p["a"])(it,Z,tt,!1,null,"76811eee",null),ot=rt.exports,at=[{path:"/",redirect:"/home"},{path:"/home",name:"home",component:L},{path:"/settings",name:"settings",component:ot},{path:"/structure",name:"structure",component:Y}],ct=at,lt=(n("92c6"),n("96cf"),n("89ba")),ut={structure:{}},pt={getHierarchy:function(t){return t.structure}},dt={completeHierarchy:function(){var t=Object(lt["a"])(regeneratorRuntime.mark((function t(e){var n,s;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return n=e.commit,t.next=3,j.a.get("http://localhost:8098/hierarchy");case 3:s=t.sent,n("setHierarchy",s.data);case 5:case"end":return t.stop()}}),t)})));function e(e){return t.apply(this,arguments)}return e}()},ft={setHierarchy:function(t,e){return t.structure=e}},mt={state:ut,actions:dt,mutations:ft,getters:pt},vt={settings:[{siteName:"Default Site Name",port:Number,siteDescription:String,bacnetSeperator:String}]},ht={newSettings:function(){var t=Object(lt["a"])(regeneratorRuntime.mark((function t(e,n){var s,i;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:s=e.commit,i=j.a.post("http://localhost:8098/settings",n),s("setSettings",i.data);case 3:case"end":return t.stop()}}),t)})));function e(e,n){return t.apply(this,arguments)}return e}(),readSettings:function(){var t=Object(lt["a"])(regeneratorRuntime.mark((function t(e){var n,s;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return n=e.commit,t.next=3,j.a.get("http://localhost:8098/settings");case 3:s=t.sent,n("backendSettings",s.data),console.log(s.data);case 6:case"end":return t.stop()}}),t)})));function e(e){return t.apply(this,arguments)}return e}()},gt={setSettings:function(t,e){return t.settings=e},backendSettings:function(t,e){return t.settings=e}},bt={getSettings:function(t){return t.settings}},_t={actions:ht,mutations:gt,getters:bt,state:vt};s["a"].use(m["a"]);var Ct=new m["a"].Store({modules:{structure:mt,settings:_t}});s["a"].config.productionTip=!1,s["a"].use(y["a"]);var yt=new y["a"]({routes:ct});new s["a"]({store:Ct,router:yt,render:function(t){return t(C)}}).$mount("#app")},"85ec":function(t,e,n){},9539:function(t,e,n){},a7f8:function(t,e,n){},ac03:function(t,e,n){"use strict";var s=n("ddd2"),i=n.n(s);i.a},be3c:function(t,e,n){"use strict";var s=n("a7f8"),i=n.n(s);i.a},ddd2:function(t,e,n){},f01e:function(t,e,n){"use strict";var s=n("9539"),i=n.n(s);i.a}});
//# sourceMappingURL=app.9b0b21d6.js.map