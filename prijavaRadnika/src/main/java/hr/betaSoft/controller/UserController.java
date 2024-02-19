package hr.betaSoft.controller;

import hr.betaSoft.model.Employee;
import hr.betaSoft.security.exception.UserNotFoundException;
import hr.betaSoft.security.secModel.User;
import hr.betaSoft.security.secService.UserService;
import hr.betaSoft.security.userdto.UserDto;
import hr.betaSoft.tools.Column;
import hr.betaSoft.tools.Data;
import hr.betaSoft.tools.DeviceDetector;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/show")
    public String showUsers(Model model, HttpServletRequest request) {

        DeviceDetector deviceDetector = new DeviceDetector();
        boolean isMobile = deviceDetector.isMobileDevice(request);

        List<Column> columnList = new ArrayList<>();

        if (isMobile) {
            columnList.add(new Column("Naziv tvrtke", "company", "id"));
            columnList.add(new Column("Korisničko ime", "username", "id"));
        } else {
            columnList.add(new Column("ID", "id", "id"));
            columnList.add(new Column("Korisničko ime", "username", "id"));
            columnList.add(new Column("OIB", "oib", "id"));
            columnList.add(new Column("Naziv tvrtke", "company", "id"));
            columnList.add(new Column("Adresa", "address", "id"));
            columnList.add(new Column("Naziv grada", "city", "id"));
            columnList.add(new Column("Osoba", "name", "id"));
            columnList.add(new Column("Telefon", "telephone", "id"));
            columnList.add(new Column("e-mail", "email", "id"));
            columnList.add(new Column("e-mail za slanje", "emailToSend", "id"));
//            columnList.add(new Column("e-mail za slanje", "smtpMail", "id"));
//            columnList.add(new Column("lozinka za slanje", "smtpPass", "id"));
        }

        List<User> userList = userService.findAll();
        model.addAttribute("dataList", userList);

        model.addAttribute("title", "Popis korisnika");
        model.addAttribute("dodajNaziv", "Dodaj korisnika");
        model.addAttribute("path", "/users");
        model.addAttribute("addLink", "/users/new");
        model.addAttribute("sendLink", "");
        model.addAttribute("updateLink", "/users/update/{id}");
        model.addAttribute("deleteLink", "/users/delete/{id}");
        model.addAttribute("columnList", columnList);
        model.addAttribute("script", "/js/script-table-users.js");

        return "table";
    }

    @GetMapping("/users/new")
    public String showAddForm(Model model) {

        UserDto userDto = (UserDto) model.getAttribute("userDto");

        if (userDto != null) {
            model.addAttribute("class", userDto);
        } else {
            model.addAttribute("class", new UserDto());
        }

        model.addAttribute("dataList", defineDataList(false, false));
        model.addAttribute("title", "Korisnik");
        model.addAttribute("dataId", "id");
        model.addAttribute("pathSave", "/users/save");
        model.addAttribute("pathShow", "/users/show");
        model.addAttribute("sendLink", "");
        model.addAttribute("pathSaveSend", "");
        model.addAttribute("script", "/js/script-form-users.js");
        return "form";
    }

    @GetMapping("users/update/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {

        try {
            UserDto user = userService.convertEntityToDto(userService.findById(id));

            model.addAttribute("class", user);
            model.addAttribute("dataList", defineDataList(true, false));
            model.addAttribute("title", "Korisnik");
            model.addAttribute("dataId", "id");
            model.addAttribute("pathSave", "/users/save");
            model.addAttribute("pathShow", "/users/show");
            model.addAttribute("sendLink", "");
            model.addAttribute("pathSaveSend", "");
            model.addAttribute("script", "/js/script-form-users.js");
            return "form";
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/users/show";
        }
    }

    @PostMapping("/users/save")
    public String addUser(@ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model, RedirectAttributes ra) {

        User usernameExists = userService.findByUsername(userDto.getUsername());

        if (usernameExists != null) {
            ra.addFlashAttribute("userDto", userDto);
            ra.addFlashAttribute("message", "Već postoji račun registriran s tim korisničkim imenom.");
            return "redirect:/users/new";
        }

        if (result.hasErrors()) {
            return showAddForm(model);
        }

        if (userDto.getId() != null) {
            userDto.setUsername(userService.findById(userDto.getId()).getUsername());
            userDto.setPassword(userService.findById(userDto.getId()).getPassword());
            userDto.setSmtpMail(userService.findById(userDto.getId()).getSmtpMail());
            userDto.setSmtpPass(userService.findById(userDto.getId()).getSmtpPass());
        }

        userService.saveUser(userDto);
        return "redirect:/users/show";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra) {
        try {
            if (id == 1) {
                ra.addFlashAttribute("message", "Nemoguće obrisati administratora!");
            } else if (userService.checkIfEmployeeUnderUserExist(id)) {
                ra.addFlashAttribute("message", "Nemoguće obrisati korisnika s unešenim radnicima!");
            } else {
                userService.deleteUser(id);
            }
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/users/show";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {

        return "access-denied";
    }

    public static List<Data> defineDataList(boolean update, boolean emplUpdate) {

        String enabled = emplUpdate ? "false" : "true";

        List<Data> dataList = new ArrayList<>();

        List<String> items = new ArrayList<>();

        dataList.add(new Data("Naziv tvrtke *", "company", "", "", "", "text", "true", enabled, items));
        ;
        dataList.add(new Data("OIB *", "oib", "", "", "", "text", "true", enabled, items));
        ;
        dataList.add(new Data("Adresa *", "address", "", "", "", "text", "true", "", items));
        ;
        dataList.add(new Data("Grad i poštanski broj *", "city", "", "", "", "text", "false", "", items));
        ;
        dataList.add(new Data("Ime *", "firstName", "", "", "", "text", "true", "", items));
        ;
        dataList.add(new Data("Prezime *", "lastName", "", "", "", "text", "true", "", items));
        ;
        dataList.add(new Data("Telefon *", "telephone", "", "", "", "text", "true", "", items));
        ;
        dataList.add(new Data("e-mail *", "email", "", "", "", "text", "true", "", items));
        ;
        dataList.add(new Data("e-mail za slanje *", "emailToSend", "", "", "", "text", "true", "", items));
        ;
//        if (!emplUpdate) {
//            dataList.add(new Data("e-mail za slanje *", "smtpMail", "", "", "", "text", "true", "", items));
//            ;
//            dataList.add(new Data("Lozinka za slanje *", "smtpPass", "", "", "", "text", "true", "", items));
//            ;
//        }
        if (!update) {
            dataList.add(new Data("Korisničko ime *", "username", "", "", "", "text", "true", "", items));
            ;
            dataList.add(new Data("Lozinka *", "password", "", "", "", "text", "true", "", items));
            ;
        }
        return dataList;
    }
}
