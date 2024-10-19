import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class ToDoController
{
    @Autowired
    private ToDoService toDoService;

    @GetMapping
    public List<ToDoObj> getAllToDoObjs()
    {
        return toDoService.getAllToDoItems();
    }

    @PostMapping
    public ToDoObj createToDoObj(@RequestBody ToDoObj toDoObj)
    {
        return toDoService.saveToDoObj(toDoObj);
    }

    @DeleteMapping("/{id}")
    public void deleteToDoObj(@PathVariable Integer id)
    {
        toDoService.deleteToDoObj(id);
    }
}