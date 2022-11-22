package com.olvera.uberants.common.dataAccess

import com.olvera.uberants.common.entities.Product

object FakeDatabase {

    fun getProducts(): List<Product> {

        val products: MutableList<Product> = ArrayList()

        products.add(Product("Classic",
            "https://upload.wikimedia.org/wikipedia/commons/4/48/Dona_sencilla_mexicana.jpg"))
        products.add(Product("Glazed",
            "https://p0.pikist.com/photos/921/203/donut-mini-donut-small-round-cake-torus-" +
                    "glaze-fat-sugar-mixture-schokoplaettchen.jpg"))
        products.add(Product("Chocolate",
            "https://cdn.pixabay.com/photo/2017/04/13/23/35/dona-2228986__340.jpg"))
        products.add(Product("Blue Berry",
            "https://cdn.pixabay.com/photo/2017/11/22/00/18/donuts-2969490_1280.jpg"))
        products.add(Product("Dark Chocolate",
            "https://cdn.pixabay.com/photo/2017/04/12/21/18/dona-2225812_1280.jpg"))
        products.add(Product("Strawberry",
            "https://live.staticflickr.com/1338/1036945312_8e12c26d99_b.jpg"))
        products.add(Product("Caramel",
            "https://p0.pxfuel.com/preview/187/392/25/cute-sweet-tasty-delicious.jpg"))
        products.add(Product("Wine",
            "https://cdn.pixabay.com/photo/2017/08/11/21/46/donuts-2632878__340.jpg"))
        products.add(Product("Penaut",
            "https://cdn.pixabay.com/photo/2014/02/17/17/26/donuts-268388__340.jpg"))
        products.add(Product("Apple",
            "https://cdn.pixabay.com/photo/2020/10/12/15/58/donuts-5649218__340.jpg"))
        products.add(Product("Special",
            "https://live.staticflickr.com/3356/3282189390_4ba32754b6_b.jpg"))

        return products
    }

}