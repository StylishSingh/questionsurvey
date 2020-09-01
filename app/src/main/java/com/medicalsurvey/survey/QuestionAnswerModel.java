package com.medicalsurvey.survey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionAnswerModel {


    /**
     * data : {"pre":[{"ques_no":"19","previous_index":0,"image":"","question":"19. In the last 7 days, have you seen any double images?","instructions":"INSTRUCTIONS: The next few questions are about double images, which some people call \"ghost\" or \"shadow\" images. By double images, we mean seeing a distorted or blurry visual image, such as the images shown below. These images may not represent exactly what you see and your symptoms may be more or less severe than what is shown.","answers":[{"nextIndex":1,"option":"Yes, but ONLY when NOT wearing glasses or contact lenses"},{"nextIndex":1,"option":"Yes, but ONLY when wearing glasses or contact lenses"},{"nextIndex":1,"option":"Yes, when wearing AND when not wearing glasses or contact lenses"},{"nextIndex":6,"option":"No, not at all"}]}],"post":[{"ques_no":"19","previous_index":0,"image":"","question":"19. In the last 7 days, have you seen any double images?","instructions":"INSTRUCTIONS: The next few questions are about double images, which some people call \"ghost\" or \"shadow\" images. By double images, we mean seeing a distorted or blurry visual image, such as the images shown below. These images may not represent exactly what you see and your symptoms may be more or less severe than what is shown.","answers":[{"nextIndex":1,"option":"Yes, but ONLY when NOT wearing glasses or contact lenses"},{"nextIndex":1,"option":"Yes, but ONLY when wearing glasses or contact lenses"},{"nextIndex":1,"option":"Yes, when wearing AND when not wearing glasses or contact lenses"},{"nextIndex":6,"option":"No, not at all"}]}]}
     */

    @SerializedName("data")
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        @SerializedName("pre")
        private List<PreBean> pre;
        @SerializedName("post")
        private List<PostBean> post;

        public List<PreBean> getPre() {
            return pre;
        }

        public void setPre(List<PreBean> pre) {
            this.pre = pre;
        }

        public List<PostBean> getPost() {
            return post;
        }

        public void setPost(List<PostBean> post) {
            this.post = post;
        }

        public static class PreBean {
            /**
             * ques_no : 19
             * previous_index : 0
             * image :
             * question : 19. In the last 7 days, have you seen any double images?
             * instructions : INSTRUCTIONS: The next few questions are about double images, which some people call "ghost" or "shadow" images. By double images, we mean seeing a distorted or blurry visual image, such as the images shown below. These images may not represent exactly what you see and your symptoms may be more or less severe than what is shown.
             * answers : [{"nextIndex":1,"option":"Yes, but ONLY when NOT wearing glasses or contact lenses"},{"nextIndex":1,"option":"Yes, but ONLY when wearing glasses or contact lenses"},{"nextIndex":1,"option":"Yes, when wearing AND when not wearing glasses or contact lenses"},{"nextIndex":6,"option":"No, not at all"}]
             */

            @SerializedName("ques_no")
            private String quesNo;
            @SerializedName("previous_index")
            private int previousIndex;
            @SerializedName("next_index")
            private int nextIndex;
            @SerializedName("answer")
            private String answer;
            @SerializedName("image")
            private String image;
            @SerializedName("question")
            private String question;
            @SerializedName("instructions")
            private String instructions;
            @SerializedName("answers")
            private List<AnswersBean> answers;

            public String getQuesNo() {
                return quesNo;
            }

            public void setQuesNo(String quesNo) {
                this.quesNo = quesNo;
            }

            public int getPreviousIndex() {
                return previousIndex;
            }

            public void setPreviousIndex(int previousIndex) {
                this.previousIndex = previousIndex;
            }

            public int getNextIndex() {
                return nextIndex;
            }

            public void setNextIndex(int nextIndex) {
                this.nextIndex = nextIndex;
            }

            public String getAnswer() {
                return answer;
            }

            public void setAnswer(String answer) {
                this.answer = answer;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getQuestion() {
                return question;
            }

            public void setQuestion(String question) {
                this.question = question;
            }

            public String getInstructions() {
                return instructions;
            }

            public void setInstructions(String instructions) {
                this.instructions = instructions;
            }

            public List<AnswersBean> getAnswers() {
                return answers;
            }

            public void setAnswers(List<AnswersBean> answers) {
                this.answers = answers;
            }


        }

        public static class PostBean {
            /**
             * ques_no : 19
             * previous_index : 0
             * image :
             * question : 19. In the last 7 days, have you seen any double images?
             * instructions : INSTRUCTIONS: The next few questions are about double images, which some people call "ghost" or "shadow" images. By double images, we mean seeing a distorted or blurry visual image, such as the images shown below. These images may not represent exactly what you see and your symptoms may be more or less severe than what is shown.
             * answers : [{"nextIndex":1,"option":"Yes, but ONLY when NOT wearing glasses or contact lenses"},{"nextIndex":1,"option":"Yes, but ONLY when wearing glasses or contact lenses"},{"nextIndex":1,"option":"Yes, when wearing AND when not wearing glasses or contact lenses"},{"nextIndex":6,"option":"No, not at all"}]
             */

            @SerializedName("ques_no")
            private String quesNo;
            @SerializedName("previous_index")
            private int previousIndex;
            @SerializedName("next_index")
            private int nextIndex;
            @SerializedName("answer")
            private String answer;
            @SerializedName("image")
            private String image;
            @SerializedName("question")
            private String question;
            @SerializedName("instructions")
            private String instructions;
            @SerializedName("answers")
            private List<AnswersBean> answers;

            public String getQuesNo() {
                return quesNo;
            }

            public void setQuesNo(String quesNo) {
                this.quesNo = quesNo;
            }

            public int getPreviousIndex() {
                return previousIndex;
            }

            public void setPreviousIndex(int previousIndex) {
                this.previousIndex = previousIndex;
            }

            public int getNextIndex() {
                return nextIndex;
            }

            public void setNextIndex(int nextIndex) {
                this.nextIndex = nextIndex;
            }

            public String getAnswer() {
                return answer;
            }

            public void setAnswer(String answer) {
                this.answer = answer;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getQuestion() {
                return question;
            }

            public void setQuestion(String question) {
                this.question = question;
            }

            public String getInstructions() {
                return instructions;
            }

            public void setInstructions(String instructions) {
                this.instructions = instructions;
            }

            public List<AnswersBean> getAnswers() {
                return answers;
            }

            public void setAnswers(List<AnswersBean> answers) {
                this.answers = answers;
            }
        }

        public static class AnswersBean {
            /**
             * nextIndex : 1
             * option : Yes, but ONLY when NOT wearing glasses or contact lenses
             */

            @SerializedName("index")
            private int index;
            @SerializedName("option")
            private String option;

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public String getOption() {
                return option;
            }

            public void setOption(String option) {
                this.option = option;
            }
        }
    }


}
