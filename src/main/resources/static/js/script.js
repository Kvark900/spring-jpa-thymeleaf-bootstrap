
var qs = decodeURIComponent(location);
var menuItem = qs.replace("http://localhost:8080","");

//making active links
$("a[href='" + menuItem + "']").parent("li").addClass("active");

if(menuItem.includes("adminPage")){
    $("a[href='/adminPage']").parent("li").addClass("active");
}
if(menuItem.includes("users")){
    $("a[href='/adminPage/users']").parent("li").addClass("active");
}
if(menuItem.includes("roles")){
    $("a[href='/adminPage/roles']").parent("li").addClass("active");
}