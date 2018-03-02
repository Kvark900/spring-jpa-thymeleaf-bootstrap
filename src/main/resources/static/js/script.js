
var qs = decodeURIComponent(location);
var menuItem = qs.replace("http://localhost:8080","");

//making active links
$("a[href='" + menuItem + "']").parent("li").addClass("active");
console.log(menuItem);

if(menuItem.includes("adminPage")){
    $("a[href='/adminPage']").parent("li").addClass("active");
}