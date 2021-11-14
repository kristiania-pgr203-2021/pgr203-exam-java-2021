package no.kristiania.addController;

import no.kristiania.http.HttpController;
import no.kristiania.http.HttpMessage;
import no.kristiania.Dao.Member;
import no.kristiania.Dao.MemberDao;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;

import static no.kristiania.http.QuestionnaireServer.logger;

public class AddNewMemberController implements HttpController {
    private final MemberDao memberDao;

    public AddNewMemberController(MemberDao memberDao) {

        this.memberDao = memberDao;
    }

    @Override
    public String getPath() {
        return "/api/newMember";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException, UnsupportedEncodingException {
        String[] requestLine = request.startLine.split(" ");
        String requestTarget = requestLine[1];

        int questionPos = requestTarget.indexOf('?');
        String query = requestTarget.substring(questionPos+1);

        String requestMethod = request.startLine.split(" ")[0];
        logger.info(requestMethod);

        if (requestMethod.equals("POST")){
            Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
            Member member = new Member();
            String firstName = AddQuestionController.decodeValue(queryMap.get("firstname"));
            member.setFirstName(firstName);
            String lastName = AddQuestionController.decodeValue(queryMap.get("lastname"));
            member.setLastName(lastName);
            String email = AddQuestionController.decodeValue(queryMap.get("email"));
            member.setEmail(email);

            for (Member checking:
                    memberDao.listAll()) {
                if (checking.getEmail().equals(email)){
                    String response = "<div style=color:red>This Email is allerede used \""+email+"\"</div><br><a href=/index.html>Return to front page</a>" +
                            " Or <a href=/addOption.html>Add new Option</a>";
                    return new HttpMessage("Http/1.1 400 Bad Reqeust", response);
                }
            }

            memberDao.save(member);
            logger.info("First name: {} , last name: {} and Email {} have been added", member.getFirstName(), member.getLastName(), member.getEmail());

            return new HttpMessage("Http/1.1 303 See other", "", "/addOption.html");

        }else {
            String setEmail = "";

            if (setEmail.isEmpty()){
                setEmail = "<h3 style=color: red>You are not registered</h3>";
            }

          if (query != null) {
                    for (Member checking:
                            memberDao.listAll()) {
                        if (checking.getEmail() != null || !checking.getEmail().isEmpty()){
                            setEmail = "<h5 style=color:green>You have been registered with this mail " +
                                    "\""+ checking.getEmail()+"\"</h5>";
                        }
                    }
            }

            return new HttpMessage("Http/1.1 200 Ok", setEmail);
        }
    }
}
