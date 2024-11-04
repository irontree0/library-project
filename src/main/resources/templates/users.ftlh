<html lang="en">
    <head>
        <link rel="stylesheet" href="/users.css", type="text/css">
    </head>
    <body>
        <div class="container">
            <h3>~users~</h3>
            <ul class="responsive-table">
                <li class="table-header">
                    <div class="col col-1">-id-</div>
                    <div class="col col-2">-name-</div>
                    <div class="col col-3">-password-</div>
                    <div class="col col-4">-role-</div>
                </li>
                <#list users as user>
                    <li class="table-row">
                        <div class="col col-1">${user.id}</div>
                        <div class="col col-2">${user.name}</div>
                        <div class="col col-3">${user.password}</div>
                        <div class="col col-4">${user.role}</div>
                    </li>
                </#list>
            </ul>
        </div>
    </body>
</html>