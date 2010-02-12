<p>
This page provides a tutorial that will prepare users for using XML serialization. Before
this tutorial is attempted it is advisable to have a look at the <a href="/download/stream/doc/javadoc">Javadoc</a>
documentation for the framework. Although there are only several annotations and objects involved in 
the serialization process the framework itself has many powerful features which this tutorial attempts to 
describe.
</p>
<?
$list = file("tutorial.list");

$anchors = array();
$titles = array();
$files = array();

foreach($list as $item) {      
   $item = trim($item);  

   if(preg_match("/\\s*#.*/", $item)) {
      continue;           
   }
   if(preg_match("/\\S+/", $item)) {                 
      if(is_file($item)) {
         $matches = head_file($item);     
         $anchors[$item] = $matches[1];
         $titles[$item] = $matches[2];
         $files[$item] = $item;
      } else {
         print("<br><b>ERROR CAN NOT FIND: $item</b>\n");
      }
   }
}
print("<ol>\n");

foreach($anchors as $item => $anchor) {
   print("<li><a href='#$anchors[$item]'>$titles[$item]</a></li>\n");
}
print("</ol>\n");
print("<br>");

foreach($files as $item => $file) {
   print("<!-- $item -->\n");        
   include($file);
}

function head_file($name) {
   $file = fopen($name, "r");
   $matches = array();
   
   while(!feof($file)) {
      $line = fgets($file);

      if(preg_match("/<a name='(.*)'><\/a><b>(.*)<\/b>/", $line, $matches)) {
         break;
      }
   }
   fclose($file);
   return $matches;
}
?>
