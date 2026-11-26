$paths = @(
    'C:\Users\Cris\Desktop\3.1.2 Guía 14_ Consumiendo una API Rest.pdf',
    'C:\Users\Cris\Desktop\3.2.2 Guía 15_ Detectando errores antes de que ocurran Testea tu App Móvil.pdf',
    'C:\Users\Cris\Desktop\Ev. Parcial 4_Encargo_Estudiante_ DSY1105.pdf'
)

foreach ($p in $paths) {
    Write-Output "=== GUIDE: $p ==="
    if (Test-Path $p) {
        $bytes = [System.IO.File]::ReadAllBytes($p)
        $text = [System.Text.Encoding]::ASCII.GetString($bytes)
        $matches = [System.Text.RegularExpressions.Regex]::Matches($text, '\(([^)]+)\)')
        $collected = @()
        foreach ($m in $matches) {
            $v = $m.Groups[1].Value
            if ($v.Length -gt 3 -and $v -match '[a-zA-Z]') {
                $collected += $v
            }
        }
        $collected | Select-Object -Unique | Select-Object -First 100
    } else {
        Write-Output "File not found"
    }
}
