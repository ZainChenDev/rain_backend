package com.zainchen.framework.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("KaptchaTextCreator 测试类")
public class KaptchaTextCreatorTest {

    private KaptchaTextCreator kaptchaTextCreator;

    // 用于匹配表达式的正则模式
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile("^(\\d+)([+\\-*/])(\\d+)=\\?@(\\d+)$");

    @BeforeEach
    void setUp() {
        kaptchaTextCreator = new KaptchaTextCreator();
    }

    @Test
    @DisplayName("测试基本功能 - 验证返回格式")
    void testBasicFormat() {
        String result = kaptchaTextCreator.getText();

        assertNotNull(result, "返回结果不应为null");
        assertTrue(result.contains("=?@"), "结果应包含 '=?@'");
        assertTrue(EXPRESSION_PATTERN.matcher(result).matches(),
                "结果格式应匹配正则表达式: " + result);
    }

    @RepeatedTest(100)
    @DisplayName("重复测试 - 验证计算正确性")
    void testCalculationCorrectness() {
        String expression = kaptchaTextCreator.getText();
        Matcher matcher = EXPRESSION_PATTERN.matcher(expression);

        assertTrue(matcher.matches(), "表达式格式错误: " + expression);

        int operand1 = Integer.parseInt(matcher.group(1));
        String operator = matcher.group(2);
        int operand2 = Integer.parseInt(matcher.group(3));
        int expectedResult = Integer.parseInt(matcher.group(4));

        int actualResult = calculateExpected(operand1, operator, operand2);

        assertEquals(expectedResult, actualResult,
                String.format("计算错误: %d %s %d 应该等于 %d，但实际为 %d",
                        operand1, operator, operand2, expectedResult, actualResult));
    }

    @Test
    @DisplayName("测试操作数范围")
    void testOperandRange() {
        for (int i = 0; i < 100; i++) {
            String expression = kaptchaTextCreator.getText();
            Matcher matcher = EXPRESSION_PATTERN.matcher(expression);

            assertTrue(matcher.matches(), "表达式格式错误: " + expression);

            int operand1 = Integer.parseInt(matcher.group(1));
            int operand2 = Integer.parseInt(matcher.group(3));

            assertTrue(operand1 >= 0 && operand1 <= 10,
                    "操作数1应在0-10范围内，实际值: " + operand1);
            assertTrue(operand2 >= 0 && operand2 <= 10,
                    "操作数2应在0-10范围内，实际值: " + operand2);
        }
    }

    @Test
    @DisplayName("测试除法特殊情况")
    void testDivisionValidation() {
        Set<String> divisionExpressions = new HashSet<>();

        // 收集足够多的表达式来检查除法
        for (int i = 0; i < 1000; i++) {
            String expression = kaptchaTextCreator.getText();
            if (expression.contains("/")) {
                divisionExpressions.add(expression);
            }
        }

        for (String expression : divisionExpressions) {
            System.out.println("Testing division expression: " + expression);
            Matcher matcher = EXPRESSION_PATTERN.matcher(expression);
            assertTrue(matcher.matches(), "除法表达式格式错误: " + expression);

            int dividend = Integer.parseInt(matcher.group(1));
            int divisor = Integer.parseInt(matcher.group(3));
            int result = Integer.parseInt(matcher.group(4));

            assertNotEquals(0, divisor, "除数不应为0: " + expression);
            assertEquals(0, dividend % divisor, "除法应能整除: " + expression);
            assertEquals(dividend / divisor, result, "除法计算错误: " + expression);
        }
    }

    @Test
    @DisplayName("测试减法结果非负")
    void testSubtractionNonNegative() {
        Set<String> subtractionExpressions = new HashSet<>();

        // 收集减法表达式
        for (int i = 0; i < 1000; i++) {
            String expression = kaptchaTextCreator.getText();
            if (expression.contains("-")) {
                subtractionExpressions.add(expression);
            }
        }

        for (String expression : subtractionExpressions) {
            Matcher matcher = EXPRESSION_PATTERN.matcher(expression);
            assertTrue(matcher.matches(), "减法表达式格式错误: " + expression);

            int result = Integer.parseInt(matcher.group(4));
            assertTrue(result >= 0, "减法结果应非负: " + expression);
        }
    }

    @Test
    @DisplayName("测试操作符分布")
    void testOperatorDistribution() {
        Set<String> operators = new HashSet<>();

        // 生成足够多的表达式来检查所有操作符
        for (int i = 0; i < 500; i++) {
            String expression = kaptchaTextCreator.getText();
            Matcher matcher = EXPRESSION_PATTERN.matcher(expression);

            if (matcher.matches()) {
                operators.add(matcher.group(2));
            }
        }

        // 验证所有四种操作符都出现过
        assertTrue(operators.contains("+"), "应包含加法操作");
        assertTrue(operators.contains("-"), "应包含减法操作");
        assertTrue(operators.contains("*"), "应包含乘法操作");
        assertTrue(operators.contains("/"), "应包含除法操作");
    }

    @Test
    @DisplayName("测试线程安全性")
    void testThreadSafety() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Set<CompletableFuture<String>> futures = new HashSet<>();

        // 创建100个并发任务
        for (int i = 0; i < 100; i++) {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                String result = kaptchaTextCreator.getText();
                // 验证格式正确性
                assertTrue(EXPRESSION_PATTERN.matcher(result).matches(),
                        "并发环境下格式错误: " + result);
                return result;
            }, executor);
            futures.add(future);
        }

        // 等待所有任务完成并验证结果
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        assertDoesNotThrow(() -> allTasks.get(), "并发执行不应抛出异常");

        executor.shutdown();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 50, 100})
    @DisplayName("参数化测试 - 批量验证")
    void testBatchGeneration(int count) {
        for (int i = 0; i < count; i++) {
            String expression = kaptchaTextCreator.getText();
            Matcher matcher = EXPRESSION_PATTERN.matcher(expression);

            assertTrue(matcher.matches(),
                    String.format("第%d次生成的表达式格式错误: %s", i + 1, expression));

            // 验证计算正确性
            int operand1 = Integer.parseInt(matcher.group(1));
            String operator = matcher.group(2);
            int operand2 = Integer.parseInt(matcher.group(3));
            int expectedResult = Integer.parseInt(matcher.group(4));

            int actualResult = calculateExpected(operand1, operator, operand2);
            assertEquals(expectedResult, actualResult,
                    String.format("第%d次计算错误: %s", i + 1, expression));
        }
    }

    @Test
    @DisplayName("测试结果范围合理性")
    void testResultRange() {
        for (int i = 0; i < 200; i++) {
            String expression = kaptchaTextCreator.getText();
            Matcher matcher = EXPRESSION_PATTERN.matcher(expression);

            assertTrue(matcher.matches(), "表达式格式错误: " + expression);

            int result = Integer.parseInt(matcher.group(4));

            // 基于操作数范围(0-10)，合理的结果范围应该是0-100
            assertTrue(result >= 0 && result <= 100,
                    "结果超出合理范围: " + expression + ", 结果: " + result);
        }
    }

    /**
     * 辅助方法 - 根据操作符计算预期结果
     */
    private int calculateExpected(int operand1, String operator, int operand2) {
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return Math.abs(operand1 - operand2);
            case "*":
                return operand1 * operand2;
            case "/":
                if (operand2 == 0) {
                    throw new ArithmeticException("除数不能为0");
                }
                return operand1 / operand2;
            default:
                throw new IllegalArgumentException("未知操作符: " + operator);
        }
    }
}