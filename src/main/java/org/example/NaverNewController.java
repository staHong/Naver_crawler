package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
@RequestMapping("/naverNews")
public class NaverNewController {

    @Autowired
    NaverNewsService naverNewsService;

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String Main() {
        return "main";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public List<NaverNewsDto> Search(@RequestParam String selectedOption, @RequestParam String searchWord) {
        // Replace this with your actual search logic
        List<NaverNewsDto> naverNewsDtoList = naverNewsService.searchByQuery(selectedOption, searchWord);
        return naverNewsDtoList;
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String Detail(int id, Model model) {
        NaverNewsDto naverNewsDto = naverNewsService.searchById(id);
        model.addAttribute("naverNewsDto", naverNewsDto);
        return "detail";
    }
}

