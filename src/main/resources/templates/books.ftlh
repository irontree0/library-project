<html lang="en">
    <head>
      <link rel="stylesheet" href="/books.css", type="text/css">
    </head>
    <body>
        <div class="container">
            <h3>~books~</h3>
            <ul class="responsive-table">
                <li class="table-header">
                    <div class="col col-1">-id-</div>
                    <div class="col col-2">-name-</div>
                    <div class="col col-3">-genre-</div>
                    <div class="col col-4">-authors-</div>
                </li>
                <#list books as book>
                    <li class="table-row">
                        <div class="col col-1">${book.id}</div>
                        <div class="col col-2">${book.name}</div>
                        <div class="col col-3">${book.genre}</div>
                        <div class="col col-4">${book.authors}</div>
                    </li>
                </#list>
            </ul>
        </div>
    </body>
</html>