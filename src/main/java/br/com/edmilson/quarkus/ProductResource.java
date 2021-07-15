package br.com.edmilson.quarkus;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("produtcs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @GET
    public List<Product> findAllProducts(){
        return Product.listAll();
    }

    @POST
    @Transactional
    public void SaveProduct(ProductDTO dto){
        Product product = new Product(dto);
        product.persist();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void updateProduct(@PathParam("id") Long id, ProductDTO dto){

        Optional<Product> productOptional = Product.findByIdOptional(id);
        if(productOptional.isPresent()){
            Product product = productOptional.get();
            product.setName(dto.getName());
            product.setPrice(dto.getPrice());
            product.persist();
        }else{
            throw new NotFoundException();
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void deleteProduct(@PathParam("id") Long id){
        Optional<Product> productOptional = Product.findByIdOptional(id);
        productOptional.ifPresentOrElse(Product::delete, ()-> {throw new NotFoundException();});
    }

}
