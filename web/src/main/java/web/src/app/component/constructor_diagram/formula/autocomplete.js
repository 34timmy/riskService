
var dinos = [
  "aardonyx", "allosaurus", "anchiceratops", "ankylosaurus", "apatosaurus",
  "arrhinoceratops", "atlascopcosaurus", "avalonia", "azendohsaurus",
  "bactrosaurus", "bagaceratops", "bambiraptor", "baryonyx", "becklespinax",
  "bellusaurus", "brachiosaurus", "brachyceratops", "buitreraptor",
  "camarasaurus", "carnotaurus", "cedarpelta", "centrosaurus", "coelophysis",
  "compsognathus", "conchoraptor", "confuciusornis", "corythosaurus",
  "deinonychus", "diplodocus", "edmontosaurus", "euoplocephalus", "fukuiraptor",
  "fukuisaurus", "gallimimus", "gigantosaurus", "giraffatitan", "hypsilophodon",
  "iguanodon", "irritator", "ichthyosaurus", "janenschia", "jaxartosaurus",
  "jinzhousaurus", "jobaria", "juravenator", "kentrosaurus", "khaan",
  "kotasaurus", "kritosaurus", "lamaceratops", "lambeosaurus", "leaellynasaura",
  "lesothosaurus", "lexovisaurus", "liaoxiornis", "lycorhinus", "macrurosaurus",
  "maiasaura", "mamenchisaurus", "megalosaurus", "minmi", "nanotyrannus",
  "nipponosaurus", "notoceratops", "nqwebasaurus", "omeisaurus", "ornitholestes",
  "omithomimus", "ornithomimus", "orodromeus", "oryctodromeus", "othnielia",
  "ouranosaurus", "oviraptor", "pachycephalosaurus", "panoplosaurus",
  "parasaurolophus", "pentaceratops", "plateosaurus", "plesiosaurus",
  "protoceratops", "psittacosaurus", "quaesitosaurus", "rebbachisaurus",
  "rhabdodon", "rinchenia", "riojasaurus", "rugops", "saurolophus",
  "saurophaganax", "seismosaurus", "spinosaurus", "stegosaurus", "stegoceras",
  "styracosaurus", "triceratops", "troodon", "tyrannosaurus", "tyrannotitan",
  "udanoceratops", "unenlagia", "utahraptor", "valdosaurus", "velociraptor",
  "vulcanodon", "wannanosaurus", "wuerhosaurus", "xiaosaurus", "yadusaurus",
  "yangchuanosaurus", "yimenosaurus", "yingshanosaurus", "yinlong",
  "yuanmousaurus", "yunnanosaurus", "zalmoxes", "zephyrosaurus", "zigongosaurus",
  "zuniceratops"
];
function MyTypeahead(id, data, opts)
{
  var lookUp = data.reduce(function(p, c){ p[c]=1; return p}, {});

  opts = opts ? opts : {};
  opts.levenshteinDistance = (undefined !== opts.levenshteinDistance) ? opts.levenshteinDistance : 3;
  opts.validChars = (undefined !== opts.validChars) ? opts.validChars : /^[a-zA-Z]+$/;
  opts.tokenfield = (undefined !== opts.tokenfield) ? opts.tokenfield : false;
  opts.vertAdjustMenu = (undefined !== opts.vertAdjustMenu) ? opts.vertAdjustMenu : false;
  opts.trigger = (undefined !== opts.trigger) ? opts.trigger : '';
  opts.delimiters = (undefined !== opts.delimiters) ? opts.delimiters : ',; \r\n';

  function extractor(query)
  {
    var result = (new RegExp('([^,; \r\n' + opts.delimiters + ']+)$')).exec(query);
    if(result && result[1])
      return result[1].trim();
    return '';
  }

  function charMatches(a, b)
  {
    var i;
    for (i = 0; i < a.length && i < b.length && a[i] == b[i]; i++)
      ;
    return i;
  }

  function levDist(a, b)
  {
    if(!a || !b)
      return (a || b).length;

    var m = [];
    for(var i = 0; i <= b.length; i++)
    {
      m[i] = [i];
      if(!i)
        continue;

      for(var j = 0; j <= a.length; j++)
      {
        m[0][j] = j;
        if(!j)
          continue;

        m[i][j] = (b.charAt(i - 1) == a.charAt(j - 1))
          ? m[i - 1][j - 1]
          : Math.min( m[i - 1][j - 1] + 1, m[i][j - 1] + 1, m[i - 1][j] + 1 );
      }
    }

    return m[b.length][a.length];
  };

  var lastUpper = false;
  function strMatcher(id, strs)
  {
    return function findMatches(q, sync, async)
    {
      var pos = $(id).caret('pos');
      q = (0 < pos) ? extractor(q.substring(0, pos)) : '';

      if (!q.length)
        return;

      if (opts.trigger.length)
      {
        if(opts.trigger != q.substr(0, opts.trigger.length))
          return;

        q = q.substr(opts.trigger.length);
      }

      if (!q.length || lookUp[q])
        return;

      if (opts.validChars && opts.validChars instanceof RegExp)
        if (!q.match(opts.validChars))
          return;

      var firstChar = q.substr(0, 1);
      lastUpper = (firstChar === firstChar.toUpperCase() && firstChar !== firstChar.toLowerCase());

      var cpos = $(id).caret('position');
      $(id).parent().find('.tt-menu').css('left', cpos.left + 'px');
      if (opts.vertAdjustMenu)
        $(id).parent().find('.tt-menu').css('top', (cpos.top + cpos.height) + 'px');

      var matches = [];
      if (opts.levenshteinDistance > q.length)
      {
        var matches = [], substrRegex = new RegExp(q, 'i');
        $.each(strs, function(i, str)
        {
          if (str.length > q.length && substrRegex.test(str))
            matches.push(str);
        });
      }

      if (opts.levenshteinDistance && !matches.length)
        matches = strs;

      var ld = {};
      matches.sort(function(a, b)
      {
        if (opts.levenshteinDistance >= q.length)
        {	var d = charMatches(b, q) - charMatches(a, q);
          if (d)
            return d;
        }

        if (!opts.levenshteinDistance)
          return 0;

        if (!ld[a])
          ld[a] = levDist(a, q);
        if (!ld[b])
          ld[b] = levDist(b, q);

        return ld[a] - ld[b];
      });

      sync(matches);
    };
  };

  var lastVal = '';
  var lastPos = 0;
  function beforeReplace(event, data)
  {
    lastVal = $(id).val();
    lastPos = $(id).caret('pos');
    return true;
  }

  function onReplace(event, data)
  {
    if (!data || !data.length)
      return;

    if (!lastVal.length)
      return;

    var root = lastVal.substr(0, lastPos);
    var post = lastVal.substr(lastPos);

    var typed = extractor(root);
    if (!lastUpper && typed.length >= root.length && 0 >= post.length)
      return;

    var str = root.substr(0, root.length - typed.length);

    str += lastUpper ? (data.substr(0, 1).toUpperCase() + data.substr(1)) : data;
    var cursorPos = str.length;

    str += post;

    $(id).val(str);
    $(id).caret('pos', cursorPos);
  }

  this.typeahead = $(id).typeahead({hint: false, highlight: false}, {'limit': 5, 'source': strMatcher(id, data)})
      .on('typeahead:beforeselect', beforeReplace)
      .on('typeahead:beforeautocomplete', beforeReplace)
      .on('typeahead:beforecursorchange', beforeReplace)
      .on('typeahead:selected', function(event,data){setTimeout(function(){ onReplace(event, data); }, 0);})
      .on('typeahead:autocompleted', onReplace)
      .on('typeahead:cursorchange', onReplace)
  ;
}

var myTypeahead = new MyTypeahead('#textbox', dinos, {levenshteinDistance: 3});

var myTypeahead = new MyTypeahead('#textarea', dinos, {levenshteinDistance: 3, vertAdjustMenu: true, trigger: '@'});
