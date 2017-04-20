package models.wrapper_models;

import models.Menu;

import java.util.Collection;

/**
 * Created by Kaempe on 07-04-2017.
 */
public class Menus
{
    private Collection<Menu> menus;

    public Menus(Collection<Menu> menus){this.menus = menus; }

    public Collection<Menu> getMenus() { return menus; }

    public void setMenus(Collection<Menu> menus) { this.menus = menus; }
}
