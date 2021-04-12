package sn.sonatel.dsi.dif.selfcare.b2c.notification.conf.batch;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.notification.conf.batch.dto.AccountMsisdn;
import sn.sonatel.dsi.dif.selfcare.b2c.service.FileInformationService;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;


@Configuration
@EnableBatchProcessing
@EnableJpaRepositories
public class BatchConfiguration {

    private static final Integer CHUNK = 1000000;
    public static final String STEP_NAME_EXPORT_INFO_USER_NAME = "processingStep";
    public static final String JOB_EXPORT_INFO_USER_NAME = "processingJobExportInfoUser";

    private final HSSFWorkbook workbook = new HSSFWorkbook();

    private final FileInformationService fileInformationService;

    public BatchConfiguration(FileInformationService fileInformationService) {
        this.fileInformationService = fileInformationService;
    }

    @Bean
    public JobLauncher asyncJobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(myTaskExecutor());
        return jobLauncher;
    }
    @Bean
    public TaskExecutor myTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(64);
        executor.setMaxPoolSize(64);
        executor.setQueueCapacity(64);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("Batch-MultiThreaded-");
        return executor;
    }

    @Bean
    public TaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(64);
        executor.setCorePoolSize(64);
        executor.setQueueCapacity(64);
        return executor;
    }

    @Bean
    public Job jobAccount(Step step, JobBuilderFactory jobBuilderFactory, JobExecutionListener listener) {
        return jobBuilderFactory.get(JOB_EXPORT_INFO_USER_NAME)
            .incrementer(new RunIdIncrementer())
            .start(step)
            .listener(listener)
            .build();
    }

    @Bean
    JobListener jobListener() {
        return new JobListener(workbook, fileInformationService);
    }

    @Bean
    public Step step(StepBuilderFactory stepBuilderFactory, ItemReader<AccountMsisdn> readerFileMsisdn,
                     AsyncItemProcessor<AccountMsisdn,AccountB2C> processor,
                     AsyncItemWriter<AccountB2C> writer
    ){
        return stepBuilderFactory.get(STEP_NAME_EXPORT_INFO_USER_NAME)
            .<AccountMsisdn, Future<AccountB2C>> chunk(CHUNK)
            .reader(readerFileMsisdn)
            .processor(processor)
            .writer(writer)
            .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<AccountMsisdn> readerFileMsisdn(@Value("#{jobParameters['sourceFile']}") String pathToFile) throws Exception {

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("msisdn");
        DefaultLineMapper<AccountMsisdn> mapper = new DefaultLineMapper<>();
        mapper.setLineTokenizer(tokenizer);
        mapper.setFieldSetMapper(new AccountMsisdnFieldSetMapper());
        mapper.afterPropertiesSet();

        FlatFileItemReader<AccountMsisdn> reader = new FlatFileItemReader<>();
        reader.setResource(new PathResource(pathToFile));
        reader.setLinesToSkip(1);
        reader.setLineMapper(mapper);
        reader.afterPropertiesSet();
        return reader;
    }

    @Bean
    public ItemWriter<AccountB2C> writer() {
        Sheet sheet = workbook.createSheet("account");
        return new InformationAccountWriter(sheet);
    }

    @Bean
    public AsyncItemProcessor<AccountMsisdn, AccountB2C> asyncProcessor(AccountItemProcessor accountItemProcessor) {
        AsyncItemProcessor<AccountMsisdn, AccountB2C> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(accountItemProcessor);
        asyncItemProcessor.setTaskExecutor(myTaskExecutor());

        return asyncItemProcessor;
    }


    @Bean
    public AsyncItemWriter<AccountB2C> asyncWriter(ItemWriter<AccountB2C> writer) {
        AsyncItemWriter<AccountB2C> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(writer);
        return asyncItemWriter;
    }

    @Bean
    public Workbook workbook() {
        return new SXSSFWorkbook();
    }

}
