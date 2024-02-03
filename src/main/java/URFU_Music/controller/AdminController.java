package URFU_Music.controller;

import URFU_Music.dto.UserDto;
import URFU_Music.entity.Song;
import URFU_Music.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final StorageService storageService;
    private final SongService songService;

    @Autowired
    public AdminController(UserService userService, StorageService storageService, SongService songService) {
        this.userService = userService;
        this.storageService = storageService;
        this.songService = songService;
    }

    @GetMapping()
    public String getAdminPage() {
        return "admin/admin";
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping("/add")
    public ModelAndView getAddSongsForm() {
        ModelAndView mav = new ModelAndView("admin/add_song");
        Song song = new Song();
        mav.addObject(song);
        return mav;
    }

    @PostMapping("/save")
    public String saveSong(@RequestParam("file") MultipartFile file, @Valid @ModelAttribute Song song,
                           BindingResult bindingResult, Model model) {
        song.setFile(file);
        if (file.isEmpty())
            bindingResult.rejectValue("file", "file.empty", "Вы не выбрали файл!");

        if (bindingResult.hasErrors()) {
            return "admin/add_song";
        }

        songService.save(song, file);
        storageService.store(file);
        return "redirect:/";
    }
}
