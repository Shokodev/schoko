(function(t){function e(e){for(var s,o,a=e[0],c=e[1],l=e[2],p=0,d=[];p<a.length;p++)o=a[p],Object.prototype.hasOwnProperty.call(i,o)&&i[o]&&d.push(i[o][0]),i[o]=0;for(s in c)Object.prototype.hasOwnProperty.call(c,s)&&(t[s]=c[s]);u&&u(e);while(d.length)d.shift()();return r.push.apply(r,l||[]),n()}function n(){for(var t,e=0;e<r.length;e++){for(var n=r[e],s=!0,a=1;a<n.length;a++){var c=n[a];0!==i[c]&&(s=!1)}s&&(r.splice(e--,1),t=o(o.s=n[0]))}return t}var s={},i={app:0},r=[];function o(e){if(s[e])return s[e].exports;var n=s[e]={i:e,l:!1,exports:{}};return t[e].call(n.exports,n,n.exports,o),n.l=!0,n.exports}o.m=t,o.c=s,o.d=function(t,e,n){o.o(t,e)||Object.defineProperty(t,e,{enumerable:!0,get:n})},o.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},o.t=function(t,e){if(1&e&&(t=o(t)),8&e)return t;if(4&e&&"object"===typeof t&&t&&t.__esModule)return t;var n=Object.create(null);if(o.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:t}),2&e&&"string"!=typeof t)for(var s in t)o.d(n,s,function(e){return t[e]}.bind(null,s));return n},o.n=function(t){var e=t&&t.__esModule?function(){return t["default"]}:function(){return t};return o.d(e,"a",e),e},o.o=function(t,e){return Object.prototype.hasOwnProperty.call(t,e)},o.p="/";var a=window["webpackJsonp"]=window["webpackJsonp"]||[],c=a.push.bind(a);a.push=e,a=a.slice();for(var l=0;l<a.length;l++)e(a[l]);var u=c;r.push([0,"chunk-vendors"]),n()})({0:function(t,e,n){t.exports=n("56d7")},"034f":function(t,e,n){"use strict";var s=n("85ec"),i=n.n(s);i.a},"35df":function(t,e,n){},"4b1e":function(t,e,n){"use strict";var s=n("35df"),i=n.n(s);i.a},"56d7":function(t,e,n){"use strict";n.r(e);n("e260"),n("e6cf"),n("cca6"),n("a79d");var s=n("2b0e"),i=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{attrs:{id:"app"}},[n("section",{staticClass:"columns is-fullheight"},[n("aside",{staticClass:"column is-narrow is-fullheight",attrs:{id:"menu"}},[n("p",{staticClass:"menu-label"},[t._v("Menu")]),n("Menu")],1),n("router-view",{staticClass:"column",attrs:{id:"content"}})],1)])},r=[],o=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("ul",{staticClass:"menu-list"},[n("li",[n("router-link",{attrs:{to:"/home"},on:{click:t.getToHome}},[t._v("Home")])],1),n("li",[n("router-link",{attrs:{to:"/alarmList"},on:{click:t.AlarmList}},[t._v("Alarmliste")])],1),n("li",[n("router-link",{attrs:{to:"/structure"},on:{click:t.getHierarchy}},[t._v("Struktur")])],1),n("li",[n("router-link",{attrs:{to:"/settings"},on:{click:t.settings}},[t._v("Einstellungen")])],1)])},a=[],c={name:"Menu",methods:{getToHome:function(){console.log("getToHome")},AlarmList:function(){console.log("AlarmList")},getHierarchy:function(){console.log("getHierarchy")},device:function(){console.log("device")},settings:function(){console.log("settings")}}},l=c,u=n("2877"),p=Object(u["a"])(l,o,a,!1,null,null,null),d=p.exports,m={name:"app",components:{Menu:d}},f=m,v=(n("034f"),Object(u["a"])(f,i,r,!1,null,null,null)),h=v.exports,b=n("8c4f"),g=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",[n("div",[n("h1",{staticClass:"subtitle is 3"},[t._v(" Home ")]),n("span",[n("strong",{staticClass:"button is-fullwidth level-left ",attrs:{id:"postsButton"},on:{click:t.goBack}},[t._v(t._s(t.posts.elementName)+" ("+t._s(t.posts.elementDescription)+")")])])]),n("div",{staticClass:"container is-fluid"},[0!==t.posts.length?n("span",t._l(t.posts.children,(function(e){return n("div",{key:e.elementName,staticClass:"button is-fullwidth level-left",attrs:{id:"childButton"},on:{click:function(n){return t.goIn(e)}}},[t._v(" "+t._s(e.elementName)+" ("+t._s(e.elementDescription)+") ")])})),0):t._e(),t.isModalVisible?n("modal",{attrs:{node:t.root},on:{close:function(e){t.isModalVisible=!1}}}):t._e()],1)])},_=[],C=(n("99af"),n("ac1f"),n("5319"),n("bc3a")),y=n.n(C),O=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"modal-mask"},[n("div",{staticClass:"modal-wrapper"},[n("div",{staticClass:"modal-container"},[n("article",{staticClass:"media"},[n("div",{staticClass:"media-content"},[n("div",{staticClass:"content"},[n("strong",[t._v("Datapoint")]),n("ul",t._l(t.bacnetObject,(function(e){return n("li",{key:e.elementName},[t._v(" "+t._s(e.propertyIdentifier)+" "+t._s(e.value)+" ")])})),0)]),t._m(0)])]),n("button",{staticClass:"button",on:{click:function(e){return t.$emit("close")}}},[t._v(" OK ")])])])])},x=[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"field has-addons has-addons-centered box"},[n("div",{staticClass:"level-left"},[n("strong",[t._v("Aktueller Wert")])]),n("p",{staticClass:"control level-right"},[n("span",{staticClass:"select"},[n("select",[n("option",[t._v("Ein")]),n("option",[t._v("Aus")])])])]),n("p",{staticClass:"control"},[n("a",{staticClass:"button is-primary"},[t._v(" Senden ")])])])}],S={name:"modal",props:["node"],data:function(){return{bacnetObject:"",status:"disconnected"}},mounted:function(){this.bacnetObject=this.node},methods:{}},w=S,j=(n("f01e"),Object(u["a"])(w,O,x,!1,null,null,null)),k=j.exports,A={name:"structure",components:{modal:k},data:function(){return{posts:{},element:"Anlage",parent:"",firstElement:!0,properties:[],isModalVisible:!1,root:[]}},mounted:function(){this.loadJSON()},methods:{loadJSON:function(){var t=this;y.a.get("http://localhost:8098/structure/"+this.element).then((function(e){t.posts=e.data,t.parent=t.posts["elementName"],t.firstElement=!1}))},goBack:function(){this.element=this.element.replace("'"+this.parent,""),this.loadJSON()},goIn:function(t){console.log(t),!1===this.isBACnetObject(t)&&(console.log(t),!1===this.firstElement&&(this.element=this.element.concat("'"+t["elementName"]),console.log(this.element),this.loadJSON()))},isBACnetObject:function(t){return console.log(t["elementType"]),"Structure Element"!==t["elementType"]&&(console.log("reading BACnet Object"),!0)}}},B=A,N=(n("86bf"),Object(u["a"])(B,g,_,!1,null,"0b919ba7",null)),D=N.exports,E=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",[n("h1",{staticClass:"subtitle is 3"},[t._v(" BACnet Struktur ")]),n("TreeBrowser",{attrs:{node:t.getHierarchy}})],1)},P=[],H=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",[n("div",{staticClass:"node",style:{"margin-left":20*t.depth+"px"},on:{click:t.nodeClicked}},[t.hasChildren?n("span",{staticClass:"type"},[t._v(" "+t._s(t.expanded?"▼":"►")+" ")]):n("span",{staticClass:"type"},[t._v(" "+t._s(t.expanded="◇")+" ")]),n("span",{style:t.getStyle(t.node)},[t._v(t._s(t.node.elementName)+" "),t.hasDescription?n("span",[t._v(" ("+t._s(t.node.elementDescription)+")")]):t._e()])]),t.expanded?n("div",t._l(t.node.children,(function(e){return n("TreeBrowser",{key:e.elementName,attrs:{node:e,depth:t.depth+1},on:{onClick:function(e){return t.$emit("onClick",e)}}})})),1):t._e()])},$=[],T=(n("a9e3"),{name:"TreeBrowser",props:{node:Object,depth:{type:Number,default:0}},data:function(){return{expanded:!1}},methods:{nodeClicked:function(){this.expanded=!this.expanded,this.hasChildren||this.$emit("onClick",this.node)},getStyle:function(t){if("undefined"!=typeof this.node.children){var e="black";return t.children||(e="red"),{color:e}}}},computed:{hasChildren:function(){return this.node.children.length>0},hasDescription:function(){return""!==this.node["elementDescription"]}}}),M=T,J=(n("4b1e"),Object(u["a"])(M,H,$,!1,null,"1a460d5e",null)),R=J.exports,I=n("2f62"),L={name:"structure",methods:Object(I["b"])(["completeHierarchy"]),computed:Object(I["c"])(["getHierarchy"]),mounted:function(){this.completeHierarchy()},components:{TreeBrowser:R}},V=L,F=Object(u["a"])(V,E,P,!1,null,"09e99b6c",null),K=F.exports,W=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",[n("p",{staticClass:"subtitle is 3"},[t._v("Einstellungen")]),n("div",{staticClass:"control"},[n("div",{staticClass:"field"},[n("label",{staticClass:"label"},[t._v("Site Name")]),n("div",{staticClass:"control"},[n("input",{directives:[{name:"model",rawName:"v-model",value:t.siteName,expression:"siteName"}],staticClass:"input",attrs:{type:"text",placeholder:"Site Name"},domProps:{value:t.siteName},on:{input:function(e){e.target.composing||(t.siteName=e.target.value)}}})]),n("p",{staticClass:"help"},[t._v("Site Namen eingeben")])]),n("div",{staticClass:"field"},[n("label",{staticClass:"label"},[t._v("Site Beschreibung")]),n("div",{staticClass:"control"},[n("input",{directives:[{name:"model",rawName:"v-model",value:t.siteDescription,expression:"siteDescription"}],staticClass:"input",attrs:{type:"text",placeholder:"Site Description"},domProps:{value:t.siteDescription},on:{input:function(e){e.target.composing||(t.siteDescription=e.target.value)}}})]),n("p",{staticClass:"help"},[t._v("Site Description eingeben")])]),n("div",{staticClass:"field"},[n("label",{staticClass:"label"},[t._v("BACnet Seperator")]),n("div",{staticClass:"control"},[n("input",{directives:[{name:"model",rawName:"v-model",value:t.bacnetSeperator,expression:"bacnetSeperator"}],staticClass:"input",attrs:{type:"text",placeholder:"'"},domProps:{value:t.bacnetSeperator},on:{input:function(e){e.target.composing||(t.bacnetSeperator=e.target.value)}}})]),n("p",{staticClass:"help"},[t._v("BACnet Seperator eingeben")])]),n("div",{staticClass:"control"},[n("label",{staticClass:"label"},[t._v("BACnet Port")]),n("div",{staticClass:"select"},[n("select",{directives:[{name:"model",rawName:"v-model",value:t.port,expression:"port"}],on:{change:function(e){var n=Array.prototype.filter.call(e.target.options,(function(t){return t.selected})).map((function(t){var e="_value"in t?t._value:t.value;return e}));t.port=e.target.multiple?n:n[0]}}},[n("option",[t._v("0xBAC0")]),n("option",[t._v("0xBAC1")]),n("option",[t._v("0xBAC2")]),n("option",[t._v("0xBAC3")]),n("option",[t._v("0xBAC4")]),n("option",[t._v("0xBAC5")]),n("option",[t._v("0xBAC6")]),n("option",[t._v("0xBAC7")]),n("option",[t._v("0xBAC8")]),n("option",[t._v("0xBAC9")]),n("option",[t._v("0xBACA")]),n("option",[t._v("0xBACB")]),n("option",[t._v("0xBACC")]),n("option",[t._v("0xBACD")]),n("option",[t._v("0xBACE")]),n("option",[t._v("0xBACF")])])]),n("p",{staticClass:"help"},[t._v("BACnet port auswählen")])]),n("div",{staticClass:"field is-grouped"},[n("div",{staticClass:"control"},[n("button",{staticClass:"button is-link",attrs:{disabled:t.siteName.length<=0},on:{click:t.save}},[t._v("Speichern")])]),t._m(0)])])])},q=[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"control"},[n("button",{staticClass:"button is-text"},[t._v("Abbrechen")])])}],z=(n("a4d3"),n("4de4"),n("4160"),n("e439"),n("dbb4"),n("b64b"),n("e25e"),n("159b"),n("2fa7"));function G(t,e){var n=Object.keys(t);if(Object.getOwnPropertySymbols){var s=Object.getOwnPropertySymbols(t);e&&(s=s.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),n.push.apply(n,s)}return n}function Q(t){for(var e=1;e<arguments.length;e++){var n=null!=arguments[e]?arguments[e]:{};e%2?G(n,!0).forEach((function(e){Object(z["a"])(t,e,n[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(n)):G(n).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(n,e))}))}return t}var U={name:"Settings",data:function(){return{siteName:"",port:47808,portSelected:"",siteDescription:"",bacnetSeperator:"'"}},methods:Q({save:function(){return y.a.post("http://localhost:8080/settings/",{siteName:this.siteName,port:parseInt(this.port),siteDescription:this.siteDescription,bacnetSeperator:this.bacnetSeperator}),this.portSelected=this.port}},Object(I["b"])(["settings","readSettings"])),computed:Object(I["c"])(["getSettings"]),created:function(){console.log(this.readSettings)}},X=U,Y=Object(u["a"])(X,W,q,!1,null,"00708198",null),Z=Y.exports,tt=[{path:"/",redirect:"/home"},{path:"/home",name:"home",component:D},{path:"/settings",name:"settings",component:Z},{path:"/structure",name:"structure",component:K}],et=tt,nt=(n("92c6"),n("96cf"),n("89ba")),st={structure:{}},it={getHierarchy:function(t){return t.structure}},rt={completeHierarchy:function(){var t=Object(nt["a"])(regeneratorRuntime.mark((function t(e){var n,s;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return n=e.commit,t.next=3,y.a.get("http://localhost:8098/hierarchy");case 3:s=t.sent,n("setHierarchy",s.data);case 5:case"end":return t.stop()}}),t)})));function e(e){return t.apply(this,arguments)}return e}()},ot={setHierarchy:function(t,e){return t.structure=e}},at={state:st,actions:rt,mutations:ot,getters:it},ct={settings:[{siteName:"Default Site Name",port:Number,siteDescription:String,bacnetSeperator:String}]},lt={settings:function(){var t=Object(nt["a"])(regeneratorRuntime.mark((function t(e,n){var s,i;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:s=e.commit,i=y.a.post("http://localhost:8098/settings",n),s("setSettings",i.data);case 3:case"end":return t.stop()}}),t)})));function e(e,n){return t.apply(this,arguments)}return e}(),readSettings:function(){var t=Object(nt["a"])(regeneratorRuntime.mark((function t(e){var n,s;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return n=e.commit,t.next=3,y.a.get("http://localhost:8098/settings");case 3:s=t.sent,n("backendSettings",s.data);case 5:case"end":return t.stop()}}),t)})));function e(e){return t.apply(this,arguments)}return e}()},ut={setSettings:function(t,e){return t.settings=e},backendSettings:function(t,e){return t.settings=e}},pt={getSettings:function(t){return t.settings}},dt={actions:lt,mutations:ut,getters:pt,state:ct};s["a"].use(I["a"]);var mt=new I["a"].Store({modules:{structure:at,settings:dt}});s["a"].config.productionTip=!1,s["a"].use(b["a"]);var ft=new b["a"]({routes:et});new s["a"]({store:mt,router:ft,render:function(t){return t(h)}}).$mount("#app")},"85ec":function(t,e,n){},"86bf":function(t,e,n){"use strict";var s=n("b542"),i=n.n(s);i.a},9539:function(t,e,n){},b542:function(t,e,n){},f01e:function(t,e,n){"use strict";var s=n("9539"),i=n.n(s);i.a}});
//# sourceMappingURL=app.3e250e88.js.map