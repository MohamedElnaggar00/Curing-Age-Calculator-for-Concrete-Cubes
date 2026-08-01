import os

f = "app/src/main/java/com/example/ui/screens/ConcreteCalcScreen.kt"
with open(f, "r") as file:
    content = file.read()

content = content.replace(
'''                                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ConcreteOption.values().forEach { option ->
                        OneUIPillButton(
                            text = stringResource(option.labelResId),
                            onClick = { viewModel.setSelectedOption(option) },
                            isSelected = selectedOption == option,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }''',
'''                                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ConcreteOption.values().forEach { option ->
                        OneUIPillButton(
                            text = stringResource(option.labelResId),
                            onClick = { viewModel.setSelectedOption(option) },
                            isSelected = selectedOption == option
                        )
                    }
                }'''
)

if "import androidx.compose.foundation.horizontalScroll" not in content:
    content = content.replace("import androidx.compose.foundation.verticalScroll", "import androidx.compose.foundation.horizontalScroll\nimport androidx.compose.foundation.verticalScroll")

with open(f, "w") as file:
    file.write(content)
