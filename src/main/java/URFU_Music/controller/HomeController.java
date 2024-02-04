package URFU_Music.controller;

import URFU_Music.service.SongService;
import URFU_Music.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class HomeController {
    private final SongService songService;
    private final UserService userService;

    @Autowired
    public HomeController(SongService songService, UserService userService) {
        this.songService = songService;
        this.userService = userService;
    }

    @GetMapping()
    public ModelAndView index(@RequestParam(required = false, defaultValue = "0") Integer page,
                              @RequestParam(required = false, defaultValue = "20") Integer songsPerPage) {
        ModelAndView mav = new ModelAndView("home/index");
        mav.addObject("songs", songService.getAll(page, songsPerPage, true));
        mav.addObject("currentPage", page);

        if (userService.findCurrentUser() != null) {
            mav.addObject("userSongs", userService.findCurrentUser().getSongs());
        }

        return mav;
    }

    @GetMapping("/search")
    public ModelAndView findTracks(@RequestParam String query,
                                   @RequestParam(required = false, defaultValue = "0") Integer page) {
        ModelAndView mav = new ModelAndView("home/index");
        mav.addObject("songs", songService.findTrack(query, true));
        mav.addObject("currentPage",page);

        if (userService.findCurrentUser() != null) {
            mav.addObject("userSongs", userService.findCurrentUser().getSongs());
        }

        return mav;
    }

}
