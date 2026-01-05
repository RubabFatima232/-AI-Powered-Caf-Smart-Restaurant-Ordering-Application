package com.aicafe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuViewModel extends ViewModel {
    private final MutableLiveData<List<MenuItem>> menuLive = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<MenuItem>> filteredMenuLive = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<MenuItem>> getFilteredMenu() {
        return filteredMenuLive;
    }

    public LiveData<List<MenuItem>> getMenu() {
        return menuLive;
    }

    public void loadMenu() {
        // load from local JSON (simulate Retrofit)
        List<MenuItem> list = MenuRepository.getMenu();
        menuLive.setValue(list);
        filteredMenuLive.setValue(list);
    }

    public void setCategory(String category) {
        List<MenuItem> fullMenu = menuLive.getValue();
        if (fullMenu == null) {
            return;
        }

        if (category.equals("All")) {
            filteredMenuLive.setValue(fullMenu);
        } else {
            List<MenuItem> filtered = fullMenu.stream()
                    .filter(item -> item.getCategory().equals(category))
                    .collect(Collectors.toList());
            filteredMenuLive.setValue(filtered);
        }
    }

    public void addItem(String name, double price, String desc, String img, String cat) {
        List<MenuItem> current = menuLive.getValue();
        if (current == null) current = new ArrayList<>();
        int id = (int) (System.currentTimeMillis() / 1000);
        current.add(new MenuItem(id, name, price, desc, img, cat));
        menuLive.setValue(current);
        filteredMenuLive.setValue(current);
        MenuRepository.saveMenu(current);
    }

    public void deleteItem(MenuItem item) {
        List<MenuItem> current = menuLive.getValue();
        if (current == null) return;
        current.removeIf(i -> i.getId() == item.getId());
        menuLive.setValue(current);
        filteredMenuLive.setValue(current);
        MenuRepository.saveMenu(current);
    }
}
