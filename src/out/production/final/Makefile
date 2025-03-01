# Nome do arquivo principal
MAIN=Main

# Diretórios do projeto
SRC_DIR=.
BUILD_DIR=build

# Diretórios das camadas
BUSINESS=$(SRC_DIR)/business
DATA=$(SRC_DIR)/data
UI=$(SRC_DIR)/ui

# Localização de todos os arquivos .java
SOURCES=$(shell find $(SRC_DIR) -name "*.java")

# Localização de todos os arquivos .class
CLASSES=$(SOURCES:$(SRC_DIR)/%.java=$(BUILD_DIR)/%.class)

# Classpath
CLASSPATH=$(BUILD_DIR):libs/mysql-connector-j-9.1.0.jar

# Comandos
JAVAC=javac
JAVA=java

# Regras principais
all: compile

# Regra para compilar todos os arquivos
compile: $(CLASSES)

# Regra para compilar cada arquivo .java individualmente
$(BUILD_DIR)/%.class: $(SRC_DIR)/%.java
	@mkdir -p $(dir $@)
	$(JAVAC) -d $(BUILD_DIR) -cp $(CLASSPATH) $<

# Regra para executar o projeto
run: compile
	$(JAVA) -cp $(CLASSPATH) $(MAIN)

# Regra para limpar arquivos .class nas pastas business, data e ui
clean:
	@echo "Limpando arquivos .class nas pastas business, data e ui..."
	@find $(BUSINESS) -type f -name "*.class" -delete
	@find $(DATA) -type f -name "*.class" -delete
	@find $(UI) -type f -name "*.class" -delete
	@echo "Limpeza concluída."
