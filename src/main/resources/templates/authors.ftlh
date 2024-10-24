<html lang="en">
    <head>
        <link rel="stylesheet" href="/authors.css", type="text/css">
    </head>
    <body>
        <div class="container">
            <h3>~authors~</h3>
            <ul class="responsive-table">
                <li class="table-header">
                    <div class="col col-1">-id-</div>
                    <div class="col col-2">-name-</div>
                    <div class="col col-3">-surname-</div>
                </li>
                <#list authors as author>
                    <li class="table-row">
                        <div class="col col-1">${author.id}</div>
                        <div class="col col-2">${author.name}</div>
                        <div class="col col-3">${author.surname}</div>
                    </li>
                </#list>
            </ul>
        </div>
    </body>
</html>