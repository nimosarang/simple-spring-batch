package com.SpringBatchTest.job.validator;

import com.SpringBatchTest.job.validator.FileParamValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * desc: 파일 이름 파라미터 전달 그리고 검증
 * run: --spring.batch.job.names=ValidateParamJob --fileName=test.csv
 */
@Configuration
@RequiredArgsConstructor
public class ValidateParam {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job validateParamJob(Step validateParamStep) {
        return jobBuilderFactory.get("ValidateParamJob")
            .incrementer(new RunIdIncrementer())
            .validator(new FileParamValidator())
            .start(validateParamStep).build();
    }

    @JobScope
    @Bean
    public Step validateParamStep(Tasklet validateParamTasklet) {
        return stepBuilderFactory.get("ValidateParamStep")
            .tasklet(validateParamTasklet)
            .build();
    }

    @StepScope
    @Bean
    public Tasklet validateParamTasklet(@Value("#{jobParameters['fileName']}") String fileName) {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
                throws Exception {
                System.out.println(fileName);
                System.out.println("벨리데이트 파람 테스크렛");
                return RepeatStatus.FINISHED;
            }
        };
    }

}
