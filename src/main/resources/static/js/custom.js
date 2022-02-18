var customScripts = {
    profile: function () {
        // portfolio
        if ($('.isotopeWrapper').length) {
            var $container = $('.isotopeWrapper');
            var $resize = $('.isotopeWrapper').attr('id');
            // initialize isotope
            $container.isotope({
                itemSelector: '.isotopeItem',
                resizable: false, // disable normal resizing
                masonry: {
                    columnWidth: $container.width() / $resize
                }
            });
        }
    },
    
    fancybox: function () {
        // fancybox
        $(".fancybox").fancybox();
    },
   
    init: function () {
        customScripts.fancybox();
    }
}
$('document').ready(function () {
    customScripts.init();
});