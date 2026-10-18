package utils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/** Deterministic local storefront used when the public practice site is unavailable. */
public final class LocalDemoServer {

    private static HttpServer server;

    private LocalDemoServer() {
    }

    public static String start() {
        try {
            server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
            server.createContext("/", LocalDemoServer::handle);
            server.start();
            return "http://127.0.0.1:" + server.getAddress().getPort();
        } catch (IOException e) {
            throw new IllegalStateException("Could not start the local demo server", e);
        }
    }

    public static void stop() {
        if (server != null) {
            server.stop(0);
            server = null;
        }
    }

    private static void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String body;
        int status;
        if ("/".equals(path)) {
            body = page("Home", homeContent());
            status = 200;
        } else if ("/products".equals(path)) {
            body = page("Products", productsContent());
            status = 200;
        } else if ("/category_products/1".equals(path)
                || "/category_products/3".equals(path)
                || "/category_products/4".equals(path)
                || "/category_products/6".equals(path)) {
            body = page(categoryTitle(path), categoryContent(path));
            status = 200;
        } else {
            body = page("Not Found", "<h1>Not Found</h1>");
            status = 404;
        }
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (var output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }

    private static String homeContent() {
        return "<header id='header' class='site-header'><a id='store-logo' class='brand' href='/' "
                + "aria-label='Website for automation practice'><img id='store-logo-image' alt='Website for automation practice' src='data:image/gif;base64,R0lGODlhAQABAAD/ACwAAAAAAQABAAACADs='><span class='brand-mark'>AE</span>"
                + "<span>Automation Exercise</span></a><nav class='site-nav' aria-label='Primary navigation'>"
                + "<a id='products-nav' href='/products'>Products</a></nav></header>"
                + "<main class='shell'><section class='hero'><div><p class='eyebrow'>LOCAL PRACTICE STORE</p>"
                + "<h1>Find your next everyday favourite.</h1><p class='hero-copy'>Explore clothing categories with a clean, reliable shopping experience.</p>"
                + "<a class='button button-primary' href='/products'>Browse products</a></div><div class='hero-art' aria-hidden='true'>"
                + "<span class='art-circle'></span><span class='art-label'>CURATED<br>COLLECTIONS</span></div></section>"
                + "<section class='section-heading'><div><p class='eyebrow'>SHOP BY CATEGORY</p><h2>What are you looking for?</h2></div>"
                + "<a class='text-link' href='/products'>View all products <span>→</span></a></section>"
                + categoryCards() + "</main>";
    }

    private static String productsContent() {
        return header("Products") + "<main class='shell'><div class='page-intro'><p class='eyebrow'>COLLECTIONS</p>"
            + "<div class='features_items'><h2 id='products-heading' class='title'>All Products</h2></div><p>Choose a category to browse the collection.</p></div>"
                + categoryCards() + "<section id='accordian' class='category-panel' aria-label='Category menu'>"
                + categorySection("Women", "category-women", "#Women", "Women", "<li><a id='subcategory-dress' href='/category_products/1'>Dress</a></li>")
                + categorySection("Men", "category-men", "#Men", "Men", "<li><a id='subcategory-tshirts' href='/category_products/3'>Tshirts</a></li><li><a id='subcategory-jeans' href='/category_products/6'>Jeans</a></li>")
                + categorySection("Kids", "category-kids", "#Kids", "Kids", "<li><a id='subcategory-kids-dress' href='/category_products/4'>Dress</a></li>")
                + "</section></main>";
    }

    private static String categoryCards() {
        return "<div class='category-grid'><a id='card-women' class='category-card women' href='/category_products/1'><span class='card-number'>01</span><strong>Women</strong><span>Dress collection</span></a>"
                + "<a id='card-men' class='category-card men' href='/products#Men'><span class='card-number'>02</span><strong>Men</strong><span>Tshirts &amp; Jeans</span></a>"
                + "<a id='card-kids' class='category-card kids' href='/category_products/4'><span class='card-number'>03</span><strong>Kids</strong><span>Playful essentials</span></a></div>";
    }

    private static String categorySection(String name, String id, String href, String label, String links) {
        return "<section class='menu-section'><h4><a id='" + id + "' href='" + href + "' aria-label='" + label + " category'>"
                + "<span class='menu-icon' aria-hidden='true'></span>" + label + "</a></h4><ul id='" + label + "'>" + links + "</ul></section>";
    }

    private static String categoryTitle(String path) {
        return switch (path) {
            case "/category_products/1" -> "Dress";
            case "/category_products/3" -> "Tshirts";
            case "/category_products/4" -> "Kids Dress";
            case "/category_products/6" -> "Jeans";
            default -> "Products";
        };
    }

    private static String categoryContent(String path) {
        String category = switch (path) {
            case "/category_products/1" -> "Women";
            case "/category_products/3", "/category_products/6" -> "Men";
            default -> "Kids";
        };
        String item = categoryTitle(path);
        return header(item) + "<main class='shell'><div id='breadcrumbs' class='breadcrumbs' aria-label='Breadcrumb'>"
                + "<ol class='breadcrumb'><li><a id='breadcrumb-products' href='/products'>Products</a></li>"
            + "<li id='breadcrumb-current' class='active'>" + category + " &gt; " + item + "</li></ol></div>"
                + "<section class='category-hero'><p class='eyebrow'>" + category.toUpperCase() + " COLLECTION</p>"
                + "<div class='features_items'><h2 id='category-heading' class='title'>" + category + " - " + item + " Products</h2></div>"
                + "<p class='category-copy'>A considered edit of pieces made for movement, comfort, and daily life.</p>"
                + "<a class='button button-primary' href='/products'>Back to products</a></section></main>";
    }

    private static String header(String current) {
        return "<header id='header' class='site-header'><a id='store-logo' class='brand' href='/' aria-label='Website for automation practice'>"
                + "<img id='store-logo-image' alt='Website for automation practice' src='data:image/gif;base64,R0lGODlhAQABAAD/ACwAAAAAAQABAAACADs='><span class='brand-mark'>AE</span><span>Automation Exercise</span></a><nav class='site-nav' aria-label='Primary navigation'>"
                + "<a id='products-nav' href='/products'>Products</a><span class='nav-current'>" + current + "</span></nav></header>";
    }

    private static String page(String title, String content) {
        return "<!doctype html><html><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1'>"
                + "<title>Automation Exercise Local - " + title + "</title><style>" + CSS + "</style></head><body>" + content + "</body></html>";
    }

    private static final String CSS = """
            :root { --ink:#18252b; --muted:#69777b; --line:#dfe7e4; --paper:#f6f8f5; --white:#fff; --teal:#176b68; --coral:#db745e; --yellow:#f0c96a; --max:1120px; }
            * { box-sizing:border-box; }
            body { margin:0; color:var(--ink); background:var(--paper); font-family:Georgia, 'Times New Roman', serif; }
            a { color:inherit; text-decoration:none; }
            .site-header { align-items:center; background:rgba(255,255,255,.94); border-bottom:1px solid var(--line); display:flex; justify-content:space-between; min-height:76px; padding:0 5vw; }
            .brand { align-items:center; display:flex; font-size:1.08rem; font-weight:bold; gap:11px; letter-spacing:.02em; }.brand img { background:var(--teal); height:34px; width:34px; }
            .brand-mark { align-items:center; background:var(--teal); color:white; display:inline-flex; font-family:Arial,sans-serif; font-size:.74rem; height:34px; justify-content:center; letter-spacing:.08em; width:34px; }
            .site-nav { align-items:center; display:flex; font-family:Arial,sans-serif; font-size:.8rem; gap:28px; letter-spacing:.1em; text-transform:uppercase; }
            .site-nav a { border-bottom:2px solid var(--coral); padding:9px 0; }
            .nav-current { color:var(--muted); }
            .shell { margin:0 auto; max-width:var(--max); padding:0 28px 72px; }
            .hero { align-items:center; background:#e6efea; display:grid; grid-template-columns:1.15fr .85fr; margin:38px 0 76px; min-height:390px; overflow:hidden; padding:58px 7%; }
            .eyebrow { color:var(--coral); font-family:Arial,sans-serif; font-size:.68rem; font-weight:bold; letter-spacing:.18em; margin:0 0 16px; }
            h1,h2 { font-weight:normal; line-height:1.04; margin:0; }
            h1 { font-size:clamp(2.7rem, 6vw, 5rem); max-width:620px; }
            h2 { font-size:2rem; }
            .hero-copy,.page-intro p,.category-copy { color:var(--muted); font-family:Arial,sans-serif; line-height:1.7; max-width:510px; }
            .button { display:inline-block; font-family:Arial,sans-serif; font-size:.72rem; font-weight:bold; letter-spacing:.12em; padding:15px 21px; text-transform:uppercase; }
            .button-primary { background:var(--teal); color:white; }
            .hero-art { align-items:center; display:flex; height:260px; justify-content:center; position:relative; }
            .art-circle { background:var(--yellow); border-radius:50%; height:220px; position:absolute; width:220px; }
            .art-label { color:var(--teal); font-family:Arial,sans-serif; font-size:.72rem; font-weight:bold; letter-spacing:.16em; line-height:1.7; position:relative; text-align:center; }
            .section-heading { align-items:end; display:flex; justify-content:space-between; margin-bottom:24px; }
            .text-link { border-bottom:1px solid var(--ink); font-family:Arial,sans-serif; font-size:.78rem; padding-bottom:5px; }
            .text-link span { color:var(--coral); font-size:1.2rem; margin-left:7px; }
            .category-grid { display:grid; gap:16px; grid-template-columns:repeat(3,1fr); }
            .category-card { display:flex; flex-direction:column; min-height:190px; padding:24px; transition:transform .2s; }
            .category-card:hover { transform:translateY(-4px); }
            .category-card.women { background:#e9d9d2; }.category-card.men { background:#c9dfdc; }.category-card.kids { background:#f0dfaa; }
            .card-number { font-family:Arial,sans-serif; font-size:.72rem; margin-bottom:auto; opacity:.65; }
            .category-card strong { font-size:1.65rem; font-weight:normal; }.category-card span:last-child { color:var(--muted); font-family:Arial,sans-serif; font-size:.78rem; margin-top:7px; }
            .page-intro { padding:58px 0 30px; }.page-intro h1 { font-size:3.5rem; }.category-panel { display:grid; gap:16px; grid-template-columns:repeat(3,1fr); margin-top:36px; }
            .menu-section { background:var(--white); border:1px solid var(--line); padding:22px; }.menu-section h4 { font-size:1.25rem; font-weight:normal; margin:0 0 18px; }.menu-section h4 a { align-items:center; display:flex; gap:10px; }.menu-icon { color:var(--coral); font-family:Arial,sans-serif; font-size:1.3rem; }.menu-icon:before { content:'+'; }.menu-section ul { list-style:none; margin:0; padding:0; }.menu-section li { border-top:1px solid var(--line); font-family:Arial,sans-serif; font-size:.88rem; padding:12px 0; }.menu-section li a:hover { color:var(--teal); }
            .breadcrumbs { border-bottom:1px solid var(--line); padding:28px 0; }.breadcrumb { display:flex; font-family:Arial,sans-serif; font-size:.72rem; gap:12px; list-style:none; margin:0; padding:0; text-transform:uppercase; }.breadcrumb li + li:before { color:var(--coral); content:'/'; margin-right:12px; }.breadcrumb a { border-bottom:1px solid var(--coral); }.category-hero { background:var(--white); margin-top:42px; padding:54px 7%; }.category-hero .features_items { margin-bottom:15px; }.category-hero h2 { font-size:clamp(2rem,5vw,4rem); }.category-copy { margin-bottom:28px; }
            @media (max-width:700px) { .site-header { align-items:flex-start; flex-direction:column; gap:16px; padding:20px 24px; }.site-nav { gap:18px; }.shell { padding:0 18px 48px; }.hero { display:block; margin:18px 0 48px; min-height:0; padding:42px 28px; }.hero-art { height:150px; margin-top:25px; }.art-circle { height:140px; width:140px; }.section-heading { align-items:flex-start; flex-direction:column; gap:18px; }.category-grid,.category-panel { grid-template-columns:1fr; }.page-intro h1 { font-size:2.8rem; } }
            """;
}
