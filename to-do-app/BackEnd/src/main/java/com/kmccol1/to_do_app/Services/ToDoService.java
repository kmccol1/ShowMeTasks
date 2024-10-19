import java.util.List;

@Service
public class ToDoService
{
    @Autowired
    private ToDoRepository toDoRepository;

    public List<ToDoObj> getAllToDoItems()
    {
        return toDoRepository.findAll();
    }

    public ToDoObj saveToDoObj(ToDoObj toDoObj)
    {
        return toDoRepository.save(toDoItem);
    }

    public void deleteToDoItem(Integer id)
    {
        toDoRepository.deleteById(id);
    }
}