# TelegramTRX

> 基于 Spring Boot + Shiro 的 Telegram 机器人后台管理系统，支持多机器人托管、可视化菜单编排与 TRC20 (USDT) 交易流程管理。

![Java](https://img.shields.io/badge/Java-1.8-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.5.15-green)
![License](https://img.shields.io/badge/License-MIT-blue)

## 项目简介

TelegramTRX 把 Telegram 机器人的运营配置从代码里搬到了后台界面上。机器人的菜单按钮、回复文案、图片视频素材、触发关键词，全部通过 Web 后台可视化配置，改文案不用重新发版；同一套服务可以同时托管多个机器人，各自独立配置。

业务侧实现了一条完整的 TRC20 交易链路：用户在 Telegram 里询价、下单、提交收款地址，系统按汇率档位计价、对接上游支付网关生成订单，付款完成后触发下发，并把链上记录回传给用户。

## ⚠️ 免责声明

本项目仅供**学习与技术研究**使用。

- 项目本身不包含任何钱包私钥管理、链上转账签名逻辑，资金下发依赖使用者自行对接的第三方支付网关；
- 数字货币兑换、支付结算等业务在多数国家和地区属于**持牌经营范畴**，使用者须自行确保其用途符合所在司法辖区的法律法规；
- 因使用本项目造成的任何直接或间接损失、法律后果，由使用者自行承担，作者不承担任何责任。

## 功能特性

### 机器人能力

| 模块 | 说明 |
|---|---|
| 多机器人托管 | 一套服务同时运行多个 Bot，后台增删即时生效，无需重启 |
| 可视化菜单编排 | 支持普通按钮 / 内联菜单 / 文本 / 图片 / 视频 / 自定义六种配置类型，树形层级管理 |
| 动态文案变量 | 回复文案中可嵌入变量，运行时替换为实时汇率、链上记录、用户订单等数据 |
| 会话状态机 | 基于 Redis 的多步会话（询价 → 下单 → 填址 → 确认），带超时自动过期 |
| 地址与金额校验 | TRC20 地址格式校验、金额合法性校验、单用户限额控制 |

### 后台管理

- **机器人管理** —— 机器人列表（Token 录入、启停）、机器人配置（菜单树编排）
- **客户管理** —— Telegram 用户档案、状态管控
- **汇率管理** —— 按金额区间分档定价
- **支付管理** —— 收款记录、下发记录、订单明细与审核流转
- **通知管理** —— 面向用户的主动推送
- **系统管理** —— 用户 / 角色 / 菜单 / 部门 / 岗位 / 字典 / 参数 / 公告（继承自 RuoYi）
- **系统监控** —— 在线用户、操作日志、服务监控、缓存监控、定时任务

## 技术栈

| 分类 | 选型 |
|---|---|
| 核心框架 | Spring Boot 2.5.15 / Java 8 |
| 安全框架 | Apache Shiro 1.12.0 |
| 持久层 | MyBatis + PageHelper + Druid 1.2.16 |
| 数据库 | MySQL 8.x |
| 缓存 | Redis (Jedis) |
| 视图层 | Thymeleaf + Bootstrap |
| 定时任务 | Quartz |
| 机器人 SDK | telegrambots 5.4.0.1 |

## 项目结构

```
TelegramTRX
├── admin        启动模块，Web 控制器与静态资源
├── framework    框架核心：Shiro 配置、数据源、AOP 切面
├── system       系统管理业务（用户/角色/菜单/字典等）
├── common       通用工具类与常量
├── bot          ★ 机器人核心：会话处理、菜单编排、支付与下发
├── quartz       定时任务调度
├── generator    代码生成器
└── sql          数据库初始化脚本
```

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.x
- Redis 3.2+

### 1. 初始化数据库

```bash
mysql -uroot -p -e "CREATE DATABASE trx DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
mysql -uroot -p trx < sql/pf_20230706.sql
mysql -uroot -p trx < sql/quartz.sql
```

### 2. 配置环境变量

所有敏感配置均通过环境变量注入，仓库内不含任何真实凭据：

```bash
cp .env.example .env
vim .env          # 填入数据库、Redis 等真实配置
set -a && source .env && set +a
```

完整的环境变量清单见 [.env.example](.env.example)。

### 3. 启动

```bash
mvn clean package -DskipTests
java -jar admin/target/powerful.jar

# 或开发模式
mvn spring-boot:run -pl admin
```

访问 `http://localhost:8090`，默认账号 `admin` / `admin123`。

> **首次登录后请立即修改默认密码。**

### 4. 接入机器人

1. 在 Telegram 中找 [@BotFather](https://t.me/BotFather) 创建机器人，取得 Token；
2. 后台【机器人管理 → 机器人列表】新增记录，填入 Bot 用户名与 Token，状态置为启用；
3. 【机器人管理 → 机器人配置】编排菜单与回复内容；
4. 【系统管理 → 参数设置】补齐以下业务参数：

| 参数键 | 说明 |
|---|---|
| `online.api.url` | 链上记录查询接口，需替换为自己的 TRC20 收款地址 |
| `api.auth.token` | 对外支付接口认证 Token，请自行生成高强度随机串 |
| `create.order.api` | 上游支付网关的创建订单接口 |
| `notify.url` | 支付结果异步回调地址 |

## 生产部署安全须知

这个项目处理资金流转，上线前请逐项确认：

- [ ] **不要把任何凭据写进配置文件**，一律走环境变量；`.env` 已在 `.gitignore` 中排除
- [ ] 修改后台默认密码 `admin123`，并为运维账号分配最小权限角色
- [ ] Druid 监控台在 `pro` 环境默认关闭（`DRUID_STAT_ENABLED=false`）。确需开启务必配置强密码与 IP 白名单——它会暴露全部 SQL 执行记录
- [ ] MySQL、Redis 不要对公网开放，Redis 必须设置密码
- [ ] 设置固定的 `SHIRO_CIPHER_KEY`，否则重启后 RememberMe Cookie 全部失效
- [ ] `api.auth.token` 使用足够长的随机串，并定期轮换
- [ ] 生产环境建议关闭 Swagger（`swagger.enabled: false`）与演示模式

## 致谢

本项目的后台管理框架基于 [RuoYi](https://gitee.com/y_project/RuoYi)（MIT License）二次开发，感谢若依团队的开源贡献。

## License

本项目采用 [MIT License](LICENSE)。
