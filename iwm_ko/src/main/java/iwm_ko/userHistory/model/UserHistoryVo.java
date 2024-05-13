package iwm_ko.userHistory.model;

public class UserHistoryVo {


    private String job_name;
    private String gui_code;
    private String gui_remark;
    private String user_id;
    private String sql_str;

    private String updateHistory;

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getGui_code() {
        return gui_code;
    }

    public void setGui_code(String gui_code) {
        this.gui_code = gui_code;
    }

    public String getGui_remark() {
        return gui_remark;
    }

    public void setGui_remark(String gui_remark) {
        this.gui_remark = gui_remark;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSql_str() {
        return sql_str;
    }

    public void setSql_str(String sql_str) {
        this.sql_str = sql_str;
    }

    public String getUpdateHistory() {
        return updateHistory;
    }

    public void setUpdateHistory(String updateHistory) {
        this.updateHistory = updateHistory;
    }

    @Override
    public String toString() {
        return "UserHistoryVo{" +
                "job_name='" + job_name + '\'' +
                ", gui_code='" + gui_code + '\'' +
                ", gui_remark='" + gui_remark + '\'' +
                ", user_id='" + user_id + '\'' +
                ", sql_str='" + sql_str + '\'' +
                ", updateHistory='" + updateHistory + '\'' +
                '}';
    }
}
