package org.example.framework.config;

import com.google.code.kaptcha.text.impl.DefaultTextCreator;

import java.util.Random;

/**
 * 验证码文本生成器<br>
 * 生成数学表达式验证码（如 1+2=?，用户需计算结果 3）
 * 支持加、减、乘、除四则运算
 */
public class KaptchaTextCreator extends DefaultTextCreator {

    private static final String[] NUMBERS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private static final ThreadLocal<Random> RANDOM = ThreadLocal.withInitial(Random::new);

    // 操作符枚举
    private enum Operation {
        ADD() {
            @Override
            int calculate(int a, int b) {
                return a + b;
            }

            @Override
            String buildExpression(int a, int b) {
                return NUMBERS[a] + "+" + NUMBERS[b];
            }
        },

        SUBTRACT() {
            @Override
            int calculate(int a, int b) {
                return Math.abs(a - b); // 确保结果非负
            }

            @Override
            String buildExpression(int a, int b) {
                // 确保被减数大于等于减数
                if (a >= b) {
                    return NUMBERS[a] + "-" + NUMBERS[b];
                } else {
                    return NUMBERS[b] + "-" + NUMBERS[a];
                }
            }
        },

        MULTIPLY() {
            @Override
            int calculate(int a, int b) {
                return a * b;
            }

            @Override
            String buildExpression(int a, int b) {
                return NUMBERS[a] + "*" + NUMBERS[b];
            }
        },

        DIVIDE() {
            @Override
            int calculate(int a, int b) {
                return a / b;
            }

            @Override
            String buildExpression(int a, int b) {
                return NUMBERS[a] + "/" + NUMBERS[b];
            }

            @Override
            boolean isValid(int a, int b) {
                return b != 0 && a % b == 0; // 确保能整除且除数不为0
            }
        };

        Operation() {
        }

        abstract int calculate(int a, int b);

        abstract String buildExpression(int a, int b);

        // 除法需要判断是否有效
        boolean isValid(int a, int b) {
            return true;
        }
    }

    @Override
    public String getText() {
        Random random = RANDOM.get();

        // 生成两个随机数
        int firstNum = random.nextInt(10);
        int secondNum = random.nextInt(10);

        // 随机选择操作符
        Operation operation = getRandomOperation(random, firstNum, secondNum);

        // 构建表达式
        String expression = operation.buildExpression(firstNum, secondNum);
        int result = operation.calculate(firstNum, secondNum);

        return expression + "=?@" + result;
    }

    /**
     * 获取随机操作符，确保操作有效
     */
    private Operation getRandomOperation(Random random, int a, int b) {
        Operation[] operations = Operation.values();

        // 最多尝试10次找到有效操作
        for (int i = 0; i < 10; i++) {
            Operation op = operations[random.nextInt(operations.length)];
            if (op.isValid(a, b)) {
                return op;
            }
        }

        // 如果都不满足，默认返回加法
        return Operation.ADD;
    }
}
