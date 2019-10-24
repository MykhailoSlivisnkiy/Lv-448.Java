<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="<c:url value="/staticResources/css/style.css"/>">

    <title>add-authors</title>
</head>
<body>
<section class="main-section">
    <div class="container">
        <div class="row justify-content-md-center">
            <div class="add-form col-xl-4 border rounded py-3">
                <h2 class="text-primary text-center">Add new author</h2>
                <form action="<c:url value="/add-author"/>" method="post">

                    <div class="form-group">
                        <label for="author-name">Name</label>
                        <input type="text" class="form-control" id="author-name"
                               name="name"
                               placeholder="Enter author name" required>
                    </div>
                    <div class="form-group">
                        <label for="author-surname">Surname</label>
                        <input type="text" class="form-control" id="author-surname"
                               name="surname"
                               placeholder="Enter surname author" required>
                    </div>

                    <div class="text-right">
                        <button type="submit" class="btn btn-primary">Create</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</section>
</body>
</html>
