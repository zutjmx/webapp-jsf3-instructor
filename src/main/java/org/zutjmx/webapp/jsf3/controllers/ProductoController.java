package org.zutjmx.webapp.jsf3.controllers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Model;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.zutjmx.webapp.jsf3.entities.Categoria;
import org.zutjmx.webapp.jsf3.entities.Producto;
import org.zutjmx.webapp.jsf3.services.ProductoService;

import java.util.List;

@Model
public class ProductoController {

    private Producto producto;

    private Long id;

    @Inject
    private ProductoService service;

    @Inject
    private FacesContext facesContext;

    @Produces
    @Model
    public String titulo() {
        return "Listado con JavaServer Faces 3.0";
    }

    @Produces
    @RequestScoped
    @Named("listado")
    public List<Producto> findAll() {
        return service.listar();
    }

    @Produces
    @Model
    public Producto producto() {
        this.producto = new Producto();
        if (id != null && id > 0) {
            service.porId(id).ifPresent(p -> {
                this.producto = p;
            });
        }
        return producto;
    }

    @Produces
    @Model
    public List<Categoria> categorias() {
        return service.listarCategorias();
    }

    public String editar(Long id) {
        this.id = id;
        return "form.xhtml";
    }

    public String guardar() {
        System.out.println(producto);
        if (producto.getId() != null && producto.getId()>0) {
            facesContext.addMessage(null,new FacesMessage("Producto " + producto.getNombre() + " actualizado con éxito."));
        } else {
            facesContext.addMessage(null,new FacesMessage("Producto " + producto.getNombre() + " creado con éxito."));
        }
        service.guardar(producto);
        return "index.xhtml?faces-redirect=true";
    }

    public String eliminar(Producto producto) {
        service.eliminar(producto.getId());
        facesContext.addMessage(null,new FacesMessage("Producto " + producto.getNombre() + " eliminado con éxito."));
        return "index.xhtml?faces-redirect=true";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
